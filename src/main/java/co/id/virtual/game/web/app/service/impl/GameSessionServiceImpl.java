package co.id.virtual.game.web.app.service.impl;

import co.id.virtual.game.web.app.dto.websocket.GameActionMessage;
import co.id.virtual.game.web.app.dto.websocket.GameActionResponse;
import co.id.virtual.game.web.app.exception.InsufficientChipsException;
import co.id.virtual.game.web.app.model.*;
import co.id.virtual.game.web.app.repository.GameRepository;
import co.id.virtual.game.web.app.repository.GameSessionRepository;
import co.id.virtual.game.web.app.repository.UserGameHistoryRepository;
import co.id.virtual.game.web.app.repository.UserRepository;
import co.id.virtual.game.web.app.service.ChipService;
import co.id.virtual.game.web.app.service.GameSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the GameSessionService interface.
 */
@Service
@Slf4j
public class GameSessionServiceImpl implements GameSessionService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameSessionRepository gameSessionRepository;
    private final UserGameHistoryRepository userGameHistoryRepository;
    private final ChipService chipService;

    @Autowired
    public GameSessionServiceImpl(
            GameRepository gameRepository,
            UserRepository userRepository,
            GameSessionRepository gameSessionRepository,
            UserGameHistoryRepository userGameHistoryRepository,
            ChipService chipService) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.gameSessionRepository = gameSessionRepository;
        this.userGameHistoryRepository = userGameHistoryRepository;
        this.chipService = chipService;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameSession> findById(UUID id) {
        return gameSessionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameSession> findAllActiveSessions() {
        return gameSessionRepository.findByStatus(SessionStatus.ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameSession> findByGame(UUID gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found with ID: " + gameId));

        return gameSessionRepository.findByGame(game);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameSession> findActiveSessionsByGame(UUID gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found with ID: " + gameId));

        return gameSessionRepository.findByGameAndStatus(game, SessionStatus.ACTIVE);
    }

    @Override
    @Transactional
    public GameSession createSession(UUID gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found with ID: " + gameId));

        // Create a new session data object
        SessionData sessionData = SessionData.builder()
                .gameState("WAITING_FOR_PLAYERS")
                .roundNumber(0)
                .players(new ArrayList<>())
                .playerChips(new HashMap<>())
                .playerStatus(new HashMap<>())
                .gameData(new HashMap<>())
                .lastActionTime(LocalDateTime.now())
                .potSize(0L)
                .actionHistory(new ArrayList<>())
                .build();

        // Create a new game session
        GameSession gameSession = GameSession.builder()
                .game(game)
                .sessionData(sessionData)
                .status(SessionStatus.ACTIVE)
                .startedAt(LocalDateTime.now())
                .gameHistory(new ArrayList<>())
                .build();

        return gameSessionRepository.save(gameSession);
    }

    @Override
    @Transactional
    public GameSession joinSession(UUID sessionId, UUID userId, Long buyIn) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Game session not found with ID: " + sessionId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Check if the session is active
        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new IllegalStateException("Game session is not active");
        }

        // Check if the user is already in the session
        if (isUserInSession(sessionId, userId)) {
            throw new IllegalStateException("User is already in the game session");
        }

        // Check if the session is full
        Game game = session.getGame();
        SessionData sessionData = session.getSessionData();
        if (sessionData.getPlayers().size() >= game.getMaxPlayers()) {
            throw new IllegalStateException("Game session is full");
        }

        // Check if the buy-in is within the game's limits
        if (buyIn < game.getMinBet() || buyIn > game.getMaxBet()) {
            throw new IllegalArgumentException("Buy-in amount must be between " + game.getMinBet() + " and " + game.getMaxBet());
        }

        // Deduct chips from the user's balance
        try {
            chipService.subtractChips(
                    userId,
                    buyIn,
                    TransactionType.GAME_LOSS,
                    "Buy-in for game session: " + sessionId,
                    sessionId
            );
        } catch (InsufficientChipsException e) {
            throw new IllegalStateException("User does not have enough chips for buy-in", e);
        }

        // Add the user to the session
        sessionData.getPlayers().add(userId);
        sessionData.getPlayerChips().put(userId, buyIn);
        sessionData.getPlayerStatus().put(userId, "ACTIVE");
        sessionData.setLastActionTime(LocalDateTime.now());

        // Create a user game history entry
        UserGameHistory history = UserGameHistory.builder()
                .user(user)
                .game(game)
                .session(session)
                .chipsBefore(user.getChipsBalance() + buyIn) // Add back the buy-in to get the balance before
                .chipsAfter(user.getChipsBalance())
                .chipsWagered(buyIn)
                .chipsWon(0L) // Will be updated when the user leaves the session
                .gameData(GameData.builder().build())
                .playedAt(LocalDateTime.now())
                .build();

        userGameHistoryRepository.save(history);
        session.getGameHistory().add(history);

        return gameSessionRepository.save(session);
    }

    @Override
    @Transactional
    public GameSession leaveSession(UUID sessionId, UUID userId) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Game session not found with ID: " + sessionId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Check if the user is in the session
        if (!isUserInSession(sessionId, userId)) {
            throw new IllegalStateException("User is not in the game session");
        }

        // Get the user's chips from the session
        SessionData sessionData = session.getSessionData();
        Long userChips = sessionData.getPlayerChips().get(userId);

        // Add chips back to the user's balance
        chipService.addChips(
                userId,
                userChips,
                TransactionType.GAME_WIN,
                "Cash out from game session: " + sessionId,
                sessionId
        );

        // Update the user's game history
        Optional<UserGameHistory> historyOpt = session.getGameHistory().stream()
                .filter(h -> h.getUser().getId().equals(userId))
                .findFirst();

        if (historyOpt.isPresent()) {
            UserGameHistory history = historyOpt.get();
            Long chipsWon = userChips - history.getChipsWagered();
            history.setChipsWon(chipsWon);
            userGameHistoryRepository.save(history);
        }

        // Remove the user from the session
        sessionData.getPlayers().remove(userId);
        sessionData.getPlayerChips().remove(userId);
        sessionData.getPlayerStatus().remove(userId);
        sessionData.setLastActionTime(LocalDateTime.now());

        // If no players left, end the session
        if (sessionData.getPlayers().isEmpty()) {
            return endSession(sessionId);
        }

        return gameSessionRepository.save(session);
    }

    @Override
    @Transactional
    public GameSession updateSessionStatus(UUID sessionId, SessionStatus status) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Game session not found with ID: " + sessionId));

        session.setStatus(status);

        // If the session is being completed or cancelled, set the end time
        if (status == SessionStatus.COMPLETED || status == SessionStatus.CANCELLED) {
            session.setEndedAt(LocalDateTime.now());
        }

        return gameSessionRepository.save(session);
    }

    @Override
    @Transactional
    public GameSession endSession(UUID sessionId) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Game session not found with ID: " + sessionId));

        // Cash out all remaining players
        SessionData sessionData = session.getSessionData();
        List<UUID> players = new ArrayList<>(sessionData.getPlayers());

        for (UUID playerId : players) {
            try {
                leaveSession(sessionId, playerId);
            } catch (Exception e) {
                // Log the error but continue with other players
                System.err.println("Error cashing out player " + playerId + ": " + e.getMessage());
            }
        }

        // Update the session status
        session.setStatus(SessionStatus.COMPLETED);
        session.setEndedAt(LocalDateTime.now());

        return gameSessionRepository.save(session);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GameSession> getUserSessionHistory(UUID userId, Pageable pageable) {
        // Check if the user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        return gameSessionRepository.findByUserId(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GameSession> getUserSessionHistoryByGame(UUID userId, UUID gameId, Pageable pageable) {
        // TODO: Add a custom query method to the GameSessionRepository to filter by user ID and game ID
        // For now, we'll just return all sessions for the user
        return getUserSessionHistory(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GameSession> getUserSessionHistory(UUID userId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        // TODO: Add a custom query method to the GameSessionRepository to filter by user ID and time range
        // For now, we'll just return all sessions for the user
        return getUserSessionHistory(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getSessionUsers(UUID sessionId) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Game session not found with ID: " + sessionId));

        List<UUID> userIds = session.getSessionData().getPlayers();
        return userIds.stream()
                .map(id -> userRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserGameHistory> getSessionHistory(UUID sessionId) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Game session not found with ID: " + sessionId));

        return userGameHistoryRepository.findBySession(session);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserInSession(UUID sessionId, UUID userId) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Game session not found with ID: " + sessionId));

        return session.getSessionData().getPlayers().contains(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveSessions() {
        return gameSessionRepository.countByStatus(SessionStatus.ACTIVE);
    }

    @Override
    @Transactional
    public GameState joinGameSession(UUID gameId, UUID userId, Long buyIn) {
        log.info("User {} joining game {} with buy-in {}", userId, gameId, buyIn);

        // Find the game
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found with ID: " + gameId));

        // Find the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Check if there's an active session for this game
        List<GameSession> activeSessions = gameSessionRepository.findByGameAndStatus(game, SessionStatus.ACTIVE);
        GameSession session;

        if (activeSessions.isEmpty()) {
            // Create a new session if none exists
            log.info("No active session found for game {}. Creating a new one", gameId);
            session = createSession(gameId);
        } else {
            // Use the first available session
            session = activeSessions.stream()
                    .filter(s -> s.getSessionData().getPlayers().size() < game.getMaxPlayers())
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("All sessions for this game are full"));
        }

        // Join the session
        if (isUserInSession(session.getId(), userId)) {
            log.info("User {} is already in session {}", userId, session.getId());
        } else {
            session = joinSession(session.getId(), userId, buyIn);
            log.info("User {} joined session {}", userId, session.getId());
        }

        // Return the game state
        return getGameState(session.getId());
    }

    @Override
    @Transactional
    public GameActionResponse processGameAction(UUID sessionId, UUID userId, GameActionMessage action) {
        log.info("Processing game action: {} from user {} in session {}", action.getAction(), userId, sessionId);

        // Find the session
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Game session not found with ID: " + sessionId));

        // Find the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Check if the user is in the session
        if (!isUserInSession(sessionId, userId)) {
            throw new IllegalStateException("User is not in the game session");
        }

        // Check if it's the user's turn (if applicable)
        SessionData sessionData = session.getSessionData();
        if (sessionData.getCurrentTurn() != null && !sessionData.getCurrentTurn().equals(userId)) {
            throw new IllegalStateException("It's not your turn");
        }

        // Process the action based on the game type
        GameActionResponse response = new GameActionResponse();
        response.setSessionId(sessionId);
        response.setUserId(userId);
        response.setUsername(user.getUsername());
        response.setAction(action.getAction());
        response.setTimestamp(LocalDateTime.now());

        try {
            // Process the action based on the game type
            Game game = session.getGame();
            switch (game.getType()) {
                case POKER:
                    processPokerAction(session, userId, action, response);
                    break;
                case BLACKJACK:
                    processBlackjackAction(session, userId, action, response);
                    break;
                case SLOTS:
                    processSlotsAction(session, userId, action, response);
                    break;
                case ROULETTE:
                    processRouletteAction(session, userId, action, response);
                    break;
                default:
                    throw new IllegalStateException("Unsupported game type: " + game.getType());
            }

            // Save the session
            gameSessionRepository.save(session);

            // Set success flag
            response.setSuccess(true);

        } catch (Exception e) {
            log.error("Error processing game action", e);
            response.setSuccess(false);
            response.setErrorMessage(e.getMessage());
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public GameState getGameState(UUID sessionId) {
        log.info("Getting game state for session {}", sessionId);

        // Find the session
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Game session not found with ID: " + sessionId));

        Game game = session.getGame();
        SessionData sessionData = session.getSessionData();

        // Build the game state
        GameState gameState = GameState.builder()
                .gameId(game.getId())
                .sessionId(session.getId())
                .gameName(game.getName())
                .gameType(game.getType())
                .state(sessionData.getGameState())
                .roundNumber(sessionData.getRoundNumber())
                .currentTurn(sessionData.getCurrentTurn())
                .players(sessionData.getPlayers())
                .playerChips(sessionData.getPlayerChips())
                .playerStatus(sessionData.getPlayerStatus())
                .lastActionTime(sessionData.getLastActionTime())
                .potSize(sessionData.getPotSize())
                .gameData(sessionData.getGameData())
                .actionHistory(sessionData.getActionHistory())
                .active(session.getStatus() == SessionStatus.ACTIVE)
                .minBet(game.getMinBet())
                .maxBet(game.getMaxBet())
                .build();

        // Get player names
        Map<UUID, String> playerNames = new HashMap<>();
        for (UUID playerId : sessionData.getPlayers()) {
            userRepository.findById(playerId).ifPresent(u -> playerNames.put(playerId, u.getUsername()));
        }
        gameState.setPlayerNames(playerNames);

        return gameState;
    }

    // Helper methods for processing game actions

    private void processPokerAction(GameSession session, UUID userId, GameActionMessage action, GameActionResponse response) {
        // TODO: Implement poker action processing
        log.info("Processing poker action: {}", action.getAction());

        // For now, just update the session data with the action
        SessionData sessionData = session.getSessionData();
        Map<String, Object> actionData = new HashMap<>();
        actionData.put("userId", userId);
        actionData.put("action", action.getAction());
        actionData.put("amount", action.getAmount());
        actionData.put("timestamp", LocalDateTime.now());

        sessionData.getActionHistory().add(actionData);
        sessionData.setLastActionTime(LocalDateTime.now());

        // Update response
        response.setAmount(action.getAmount());
        response.setBalanceAfter(sessionData.getPlayerChips().get(userId));
    }

    private void processBlackjackAction(GameSession session, UUID userId, GameActionMessage action, GameActionResponse response) {
        // TODO: Implement blackjack action processing
        log.info("Processing blackjack action: {}", action.getAction());

        // For now, just update the session data with the action
        SessionData sessionData = session.getSessionData();
        Map<String, Object> actionData = new HashMap<>();
        actionData.put("userId", userId);
        actionData.put("action", action.getAction());
        actionData.put("amount", action.getAmount());
        actionData.put("timestamp", LocalDateTime.now());

        sessionData.getActionHistory().add(actionData);
        sessionData.setLastActionTime(LocalDateTime.now());

        // Update response
        response.setAmount(action.getAmount());
        response.setBalanceAfter(sessionData.getPlayerChips().get(userId));
    }

    private void processSlotsAction(GameSession session, UUID userId, GameActionMessage action, GameActionResponse response) {
        // TODO: Implement slots action processing
        log.info("Processing slots action: {}", action.getAction());

        // For now, just update the session data with the action
        SessionData sessionData = session.getSessionData();
        Map<String, Object> actionData = new HashMap<>();
        actionData.put("userId", userId);
        actionData.put("action", action.getAction());
        actionData.put("amount", action.getAmount());
        actionData.put("timestamp", LocalDateTime.now());

        sessionData.getActionHistory().add(actionData);
        sessionData.setLastActionTime(LocalDateTime.now());

        // Update response
        response.setAmount(action.getAmount());
        response.setBalanceAfter(sessionData.getPlayerChips().get(userId));
    }

    private void processRouletteAction(GameSession session, UUID userId, GameActionMessage action, GameActionResponse response) {
        // TODO: Implement roulette action processing
        log.info("Processing roulette action: {}", action.getAction());

        // For now, just update the session data with the action
        SessionData sessionData = session.getSessionData();
        Map<String, Object> actionData = new HashMap<>();
        actionData.put("userId", userId);
        actionData.put("action", action.getAction());
        actionData.put("amount", action.getAmount());
        actionData.put("timestamp", LocalDateTime.now());

        sessionData.getActionHistory().add(actionData);
        sessionData.setLastActionTime(LocalDateTime.now());

        // Update response
        response.setAmount(action.getAmount());
        response.setBalanceAfter(sessionData.getPlayerChips().get(userId));
    }
}
