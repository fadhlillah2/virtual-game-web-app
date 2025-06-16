package co.id.virtual.game.web.app.repository;

import co.id.virtual.game.web.app.model.Tournament;
import co.id.virtual.game.web.app.model.TournamentParticipant;
import co.id.virtual.game.web.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for TournamentParticipant entities.
 */
@Repository
public interface TournamentParticipantRepository extends JpaRepository<TournamentParticipant, UUID> {
    
    /**
     * Find a participant by tournament and user.
     *
     * @param tournament the tournament
     * @param user the user
     * @return the participant, if it exists
     */
    Optional<TournamentParticipant> findByTournamentAndUser(Tournament tournament, User user);
    
    /**
     * Find participants by tournament.
     *
     * @param tournament the tournament
     * @return the list of participants
     */
    List<TournamentParticipant> findByTournament(Tournament tournament);
    
    /**
     * Find participants by tournament, ordered by score.
     *
     * @param tournament the tournament
     * @return the list of participants
     */
    List<TournamentParticipant> findByTournamentOrderByScoreDesc(Tournament tournament);
    
    /**
     * Find participants by tournament and elimination status.
     *
     * @param tournament the tournament
     * @param eliminated whether the participant is eliminated
     * @return the list of participants
     */
    List<TournamentParticipant> findByTournamentAndEliminated(Tournament tournament, boolean eliminated);
    
    /**
     * Find participants by user.
     *
     * @param user the user
     * @param pageable the pagination information
     * @return a page of participants
     */
    Page<TournamentParticipant> findByUser(User user, Pageable pageable);
    
    /**
     * Find participants by user and elimination status.
     *
     * @param user the user
     * @param eliminated whether the participant is eliminated
     * @return the list of participants
     */
    List<TournamentParticipant> findByUserAndEliminated(User user, boolean eliminated);
    
    /**
     * Count participants by tournament.
     *
     * @param tournament the tournament
     * @return the number of participants
     */
    long countByTournament(Tournament tournament);
    
    /**
     * Count participants by tournament and elimination status.
     *
     * @param tournament the tournament
     * @param eliminated whether the participant is eliminated
     * @return the number of participants
     */
    long countByTournamentAndEliminated(Tournament tournament, boolean eliminated);
    
    /**
     * Find the top participants by tournament.
     *
     * @param tournamentId the tournament ID
     * @param limit the maximum number of participants to return
     * @return the list of top participants
     */
    @Query("SELECT p FROM TournamentParticipant p WHERE p.tournament.id = :tournamentId ORDER BY p.score DESC LIMIT :limit")
    List<TournamentParticipant> findTopParticipantsByTournament(@Param("tournamentId") UUID tournamentId, @Param("limit") int limit);
    
    /**
     * Check if a user is registered for a tournament.
     *
     * @param tournament the tournament
     * @param user the user
     * @return true if the user is registered, false otherwise
     */
    boolean existsByTournamentAndUser(Tournament tournament, User user);
}
