package co.id.virtual.game.web.app.repository;

import co.id.virtual.game.web.app.model.GameType;
import co.id.virtual.game.web.app.model.Tournament;
import co.id.virtual.game.web.app.model.TournamentStatus;
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
 * Repository for Tournament entities.
 */
@Repository
public interface TournamentRepository extends JpaRepository<Tournament, UUID> {
    
    /**
     * Find tournaments by status.
     *
     * @param status the tournament status
     * @return the list of tournaments
     */
    List<Tournament> findByStatus(TournamentStatus status);
    
    /**
     * Find tournaments by status and game type.
     *
     * @param status the tournament status
     * @param gameType the game type
     * @return the list of tournaments
     */
    List<Tournament> findByStatusAndGameType(TournamentStatus status, GameType gameType);
    
    /**
     * Find tournaments by status and start time after the given time.
     *
     * @param status the tournament status
     * @param startTime the start time
     * @return the list of tournaments
     */
    List<Tournament> findByStatusAndStartTimeAfter(TournamentStatus status, LocalDateTime startTime);
    
    /**
     * Find tournaments by status and start time before the given time.
     *
     * @param status the tournament status
     * @param startTime the start time
     * @return the list of tournaments
     */
    List<Tournament> findByStatusAndStartTimeBefore(TournamentStatus status, LocalDateTime startTime);
    
    /**
     * Find tournaments by entry fee less than or equal to the given amount.
     *
     * @param entryFee the entry fee
     * @return the list of tournaments
     */
    List<Tournament> findByEntryFeeLessThanEqual(int entryFee);
    
    /**
     * Find tournaments by game type.
     *
     * @param gameType the game type
     * @return the list of tournaments
     */
    List<Tournament> findByGameType(GameType gameType);
    
    /**
     * Find upcoming tournaments (registration open and start time in the future).
     *
     * @param now the current time
     * @return the list of upcoming tournaments
     */
    @Query("SELECT t FROM Tournament t WHERE t.status = 'REGISTRATION_OPEN' AND t.startTime > :now ORDER BY t.startTime ASC")
    List<Tournament> findUpcomingTournaments(@Param("now") LocalDateTime now);
    
    /**
     * Find active tournaments (in progress).
     *
     * @return the list of active tournaments
     */
    List<Tournament> findByStatusOrderByStartTimeDesc(TournamentStatus status);
    
    /**
     * Find completed tournaments.
     *
     * @param pageable the pagination information
     * @return a page of completed tournaments
     */
    Page<Tournament> findByStatusOrderByEndTimeDesc(TournamentStatus status, Pageable pageable);
    
    /**
     * Find tournaments by name containing the given text.
     *
     * @param name the name to search for
     * @param pageable the pagination information
     * @return a page of tournaments
     */
    Page<Tournament> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    /**
     * Count tournaments by status.
     *
     * @param status the tournament status
     * @return the number of tournaments
     */
    long countByStatus(TournamentStatus status);
    
    /**
     * Count tournaments by game type.
     *
     * @param gameType the game type
     * @return the number of tournaments
     */
    long countByGameType(GameType gameType);
}
