package co.id.virtual.game.web.app.repository;

import co.id.virtual.game.web.app.model.Game;
import co.id.virtual.game.web.app.model.GameSession;
import co.id.virtual.game.web.app.model.User;
import co.id.virtual.game.web.app.model.UserGameHistory;
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
 * Repository interface for UserGameHistory entity.
 * Provides methods to access and manipulate user game history data.
 */
@Repository
public interface UserGameHistoryRepository extends JpaRepository<UserGameHistory, UUID> {
    
    /**
     * Find all game history entries for a specific user.
     *
     * @param user the user to search for
     * @param pageable pagination information
     * @return a page of game history entries for the specified user
     */
    Page<UserGameHistory> findByUser(User user, Pageable pageable);
    
    /**
     * Find all game history entries for a specific user ID.
     *
     * @param userId the ID of the user to search for
     * @param pageable pagination information
     * @return a page of game history entries for the specified user ID
     */
    Page<UserGameHistory> findByUserId(UUID userId, Pageable pageable);
    
    /**
     * Find all game history entries for a specific game.
     *
     * @param game the game to search for
     * @param pageable pagination information
     * @return a page of game history entries for the specified game
     */
    Page<UserGameHistory> findByGame(Game game, Pageable pageable);
    
    /**
     * Find all game history entries for a specific game session.
     *
     * @param session the game session to search for
     * @return a list of game history entries for the specified game session
     */
    List<UserGameHistory> findBySession(GameSession session);
    
    /**
     * Find all game history entries for a specific user and game.
     *
     * @param user the user to search for
     * @param game the game to search for
     * @param pageable pagination information
     * @return a page of game history entries for the specified user and game
     */
    Page<UserGameHistory> findByUserAndGame(User user, Game game, Pageable pageable);
    
    /**
     * Find all game history entries for a specific user ID and game ID.
     *
     * @param userId the ID of the user to search for
     * @param gameId the ID of the game to search for
     * @param pageable pagination information
     * @return a page of game history entries for the specified user ID and game ID
     */
    Page<UserGameHistory> findByUserIdAndGameId(UUID userId, UUID gameId, Pageable pageable);
    
    /**
     * Find all game history entries that occurred within a specific time range.
     *
     * @param startTime the start of the time range
     * @param endTime the end of the time range
     * @param pageable pagination information
     * @return a page of game history entries that occurred within the specified time range
     */
    Page<UserGameHistory> findByPlayedAtBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    
    /**
     * Calculate the total chips won by a specific user.
     *
     * @param userId the ID of the user
     * @return the total chips won by the specified user
     */
    @Query("SELECT SUM(ugh.chipsWon) FROM UserGameHistory ugh WHERE ugh.user.id = :userId")
    Long sumChipsWonByUserId(@Param("userId") UUID userId);
    
    /**
     * Calculate the total chips wagered by a specific user.
     *
     * @param userId the ID of the user
     * @return the total chips wagered by the specified user
     */
    @Query("SELECT SUM(ugh.chipsWagered) FROM UserGameHistory ugh WHERE ugh.user.id = :userId")
    Long sumChipsWageredByUserId(@Param("userId") UUID userId);
    
    /**
     * Find the most recent game history entries.
     *
     * @param pageable pagination information
     * @return a page of the most recent game history entries
     */
    Page<UserGameHistory> findAllByOrderByPlayedAtDesc(Pageable pageable);
}
