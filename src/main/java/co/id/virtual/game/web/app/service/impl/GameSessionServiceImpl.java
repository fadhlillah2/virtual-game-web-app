package co.id.virtual.game.web.app.service.impl;

import co.id.virtual.game.web.app.model.*;
import co.id.virtual.game.web.app.repository.GameRepository;
import co.id.virtual.game.web.app.repository.GameSessionRepository;
import co.id.virtual.game.web.app.repository.UserGameHistoryRepository;
import co.id.virtual.game.web.app.repository.UserRepository;
import co.id.virtual.game.web.app.service.ChipService;
import co.id.virtual.game.web.app.service.GameSessionService;
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
        chipService.updateBalance(
                userId,
                -buyIn,
                TransactionType.GAME_LOSS,
                "Buy-in for game session: " + sessionId,
                sessionId
        );
        
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
        chipService.updateBalance(
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
}
