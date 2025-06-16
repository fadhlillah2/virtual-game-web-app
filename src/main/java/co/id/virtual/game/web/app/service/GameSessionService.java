package co.id.virtual.game.web.app.service;

import co.id.virtual.game.web.app.dto.websocket.GameActionMessage;
import co.id.virtual.game.web.app.dto.websocket.GameActionResponse;
import co.id.virtual.game.web.app.model.Game;
import co.id.virtual.game.web.app.model.GameSession;
import co.id.virtual.game.web.app.model.GameState;
import co.id.virtual.game.web.app.model.SessionStatus;
import co.id.virtual.game.web.app.model.User;
import co.id.virtual.game.web.app.model.UserGameHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing game sessions.
 */
public interface GameSessionService {

    /**
     * Find a game session by ID.
     *
     * @param id the ID of the game session to find
     * @return an Optional containing the game session if found, or empty if not found
     */
    Optional<GameSession> findById(UUID id);

    /**
     * Find all active game sessions.
     *
     * @return a list of all active game sessions
     */
    List<GameSession> findAllActiveSessions();

    /**
     * Find all game sessions for a specific game.
     *
     * @param gameId the ID of the game
     * @return a list of all game sessions for the specified game
     */
    List<GameSession> findByGame(UUID gameId);

    /**
     * Find all active game sessions for a specific game.
     *
     * @param gameId the ID of the game
     * @return a list of all active game sessions for the specified game
     */
    List<GameSession> findActiveSessionsByGame(UUID gameId);

    /**
     * Create a new game session.
     *
     * @param gameId the ID of the game
     * @return the created game session
     * @throws IllegalArgumentException if the game does not exist
     */
    GameSession createSession(UUID gameId);

    /**
     * Join a game session.
     *
     * @param sessionId the ID of the game session
     * @param userId the ID of the user
     * @param buyIn the amount of chips to buy in with
     * @return the updated game session
     * @throws IllegalArgumentException if the session or user does not exist
     * @throws IllegalStateException if the session is full or not active
     * @throws IllegalStateException if the user does not have enough chips
     */
    GameSession joinSession(UUID sessionId, UUID userId, Long buyIn);

    /**
     * Leave a game session.
     *
     * @param sessionId the ID of the game session
     * @param userId the ID of the user
     * @return the updated game session
     * @throws IllegalArgumentException if the session or user does not exist
     * @throws IllegalStateException if the user is not in the session
     */
    GameSession leaveSession(UUID sessionId, UUID userId);

    /**
     * Update a game session's status.
     *
     * @param sessionId the ID of the game session
     * @param status the new status
     * @return the updated game session
     * @throws IllegalArgumentException if the session does not exist
     */
    GameSession updateSessionStatus(UUID sessionId, SessionStatus status);

    /**
     * End a game session.
     *
     * @param sessionId the ID of the game session
     * @return the updated game session
     * @throws IllegalArgumentException if the session does not exist
     */
    GameSession endSession(UUID sessionId);

    /**
     * Get a user's game session history.
     *
     * @param userId the ID of the user
     * @param pageable pagination information
     * @return a page of the user's game session history
     */
    Page<GameSession> getUserSessionHistory(UUID userId, Pageable pageable);

    /**
     * Get a user's game session history for a specific game.
     *
     * @param userId the ID of the user
     * @param gameId the ID of the game
     * @param pageable pagination information
     * @return a page of the user's game session history for the specified game
     */
    Page<GameSession> getUserSessionHistoryByGame(UUID userId, UUID gameId, Pageable pageable);

    /**
     * Get a user's game session history for a specific time range.
     *
     * @param userId the ID of the user
     * @param startTime the start of the time range
     * @param endTime the end of the time range
     * @param pageable pagination information
     * @return a page of the user's game session history for the specified time range
     */
    Page<GameSession> getUserSessionHistory(UUID userId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * Get all users in a game session.
     *
     * @param sessionId the ID of the game session
     * @return a list of all users in the specified game session
     * @throws IllegalArgumentException if the session does not exist
     */
    List<User> getSessionUsers(UUID sessionId);

    /**
     * Get all game history entries for a game session.
     *
     * @param sessionId the ID of the game session
     * @return a list of all game history entries for the specified game session
     * @throws IllegalArgumentException if the session does not exist
     */
    List<UserGameHistory> getSessionHistory(UUID sessionId);

    /**
     * Check if a user is in a game session.
     *
     * @param sessionId the ID of the game session
     * @param userId the ID of the user
     * @return true if the user is in the session, false otherwise
     */
    boolean isUserInSession(UUID sessionId, UUID userId);

    /**
     * Count the number of active game sessions.
     *
     * @return the number of active game sessions
     */
    long countActiveSessions();

    /**
     * Join a game session via WebSocket.
     *
     * @param gameId the ID of the game
     * @param userId the ID of the user
     * @param buyIn the amount of chips to buy in with
     * @return the current state of the game
     * @throws IllegalArgumentException if the game or user does not exist
     * @throws IllegalStateException if no active session exists for the game or the session is full
     * @throws IllegalStateException if the user does not have enough chips
     */
    GameState joinGameSession(UUID gameId, UUID userId, Long buyIn);

    /**
     * Process a game action.
     *
     * @param sessionId the ID of the game session
     * @param userId the ID of the user
     * @param action the game action
     * @return the response to the action
     * @throws IllegalArgumentException if the session or user does not exist
     * @throws IllegalStateException if the user is not in the session or it's not the user's turn
     */
    GameActionResponse processGameAction(UUID sessionId, UUID userId, GameActionMessage action);

    /**
     * Get the current state of a game.
     *
     * @param sessionId the ID of the game session
     * @return the current state of the game
     * @throws IllegalArgumentException if the session does not exist
     */
    GameState getGameState(UUID sessionId);
}
