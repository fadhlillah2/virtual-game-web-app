package co.id.virtual.game.web.app.repository;

import co.id.virtual.game.web.app.model.Game;
import co.id.virtual.game.web.app.model.GameSession;
import co.id.virtual.game.web.app.model.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for GameSession entity.
 * Provides methods to access and manipulate game session data.
 */
@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, UUID> {
    
    /**
     * Find all active game sessions.
     *
     * @return a list of active game sessions
     */
    List<GameSession> findByStatus(SessionStatus status);
    
    /**
     * Find all game sessions for a specific game.
     *
     * @param game the game to search for
     * @return a list of game sessions for the specified game
     */
    List<GameSession> findByGame(Game game);
    
    /**
     * Find all active game sessions for a specific game.
     *
     * @param game the game to search for
     * @param status the session status to search for
     * @return a list of active game sessions for the specified game
     */
    List<GameSession> findByGameAndStatus(Game game, SessionStatus status);
    
    /**
     * Find all game sessions that started within a specific time range.
     *
     * @param startTime the start of the time range
     * @param endTime the end of the time range
     * @return a list of game sessions that started within the specified time range
     */
    List<GameSession> findByStartedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Find all game sessions that ended within a specific time range.
     *
     * @param startTime the start of the time range
     * @param endTime the end of the time range
     * @return a list of game sessions that ended within the specified time range
     */
    List<GameSession> findByEndedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Find all game sessions that a specific user participated in.
     *
     * @param userId the ID of the user
     * @param pageable pagination information
     * @return a page of game sessions that the specified user participated in
     */
    @Query("SELECT gs FROM GameSession gs JOIN gs.gameHistory gh WHERE gh.user.id = :userId")
    Page<GameSession> findByUserId(@Param("userId") UUID userId, Pageable pageable);
    
    /**
     * Count the number of active game sessions.
     *
     * @return the number of active game sessions
     */
    long countByStatus(SessionStatus status);
    
    /**
     * Find the most recent game sessions.
     *
     * @param pageable pagination information
     * @return a page of the most recent game sessions
     */
    Page<GameSession> findAllByOrderByStartedAtDesc(Pageable pageable);
}
