package co.id.virtual.game.web.app.service;

import co.id.virtual.game.web.app.dto.tournament.TournamentDto;
import co.id.virtual.game.web.app.dto.tournament.TournamentParticipantDto;
import co.id.virtual.game.web.app.model.GameType;
import co.id.virtual.game.web.app.model.Tournament;
import co.id.virtual.game.web.app.model.TournamentParticipant;
import co.id.virtual.game.web.app.model.TournamentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Service for handling tournament operations.
 */
public interface TournamentService {
    
    /**
     * Get all tournaments.
     *
     * @param pageable the pagination information
     * @return a page of tournaments
     */
    Page<TournamentDto> getAllTournaments(Pageable pageable);
    
    /**
     * Get a tournament by ID.
     *
     * @param tournamentId the tournament ID
     * @return the tournament
     * @throws IllegalArgumentException if the tournament is not found
     */
    TournamentDto getTournamentById(UUID tournamentId);
    
    /**
     * Get tournaments by status.
     *
     * @param status the tournament status
     * @return the list of tournaments
     */
    List<TournamentDto> getTournamentsByStatus(TournamentStatus status);
    
    /**
     * Get tournaments by game type.
     *
     * @param gameType the game type
     * @return the list of tournaments
     */
    List<TournamentDto> getTournamentsByGameType(GameType gameType);
    
    /**
     * Get upcoming tournaments.
     *
     * @return the list of upcoming tournaments
     */
    List<TournamentDto> getUpcomingTournaments();
    
    /**
     * Get active tournaments.
     *
     * @return the list of active tournaments
     */
    List<TournamentDto> getActiveTournaments();
    
    /**
     * Get completed tournaments.
     *
     * @param pageable the pagination information
     * @return a page of completed tournaments
     */
    Page<TournamentDto> getCompletedTournaments(Pageable pageable);
    
    /**
     * Create a new tournament.
     *
     * @param tournament the tournament to create
     * @return the created tournament
     */
    Tournament createTournament(Tournament tournament);
    
    /**
     * Update a tournament.
     *
     * @param tournamentId the tournament ID
     * @param tournament the updated tournament
     * @return the updated tournament
     * @throws IllegalArgumentException if the tournament is not found
     */
    Tournament updateTournament(UUID tournamentId, Tournament tournament);
    
    /**
     * Delete a tournament.
     *
     * @param tournamentId the tournament ID
     * @throws IllegalArgumentException if the tournament is not found
     */
    void deleteTournament(UUID tournamentId);
    
    /**
     * Register a user for a tournament.
     *
     * @param tournamentId the tournament ID
     * @param userId the user ID
     * @return the tournament participant
     * @throws IllegalArgumentException if the tournament is not found or the user is already registered
     * @throws IllegalStateException if the tournament is not open for registration
     */
    TournamentParticipant registerForTournament(UUID tournamentId, UUID userId);
    
    /**
     * Unregister a user from a tournament.
     *
     * @param tournamentId the tournament ID
     * @param userId the user ID
     * @throws IllegalArgumentException if the tournament is not found or the user is not registered
     * @throws IllegalStateException if the tournament has already started
     */
    void unregisterFromTournament(UUID tournamentId, UUID userId);
    
    /**
     * Get the participants of a tournament.
     *
     * @param tournamentId the tournament ID
     * @return the list of participants
     * @throws IllegalArgumentException if the tournament is not found
     */
    List<TournamentParticipantDto> getTournamentParticipants(UUID tournamentId);
    
    /**
     * Get the top participants of a tournament.
     *
     * @param tournamentId the tournament ID
     * @param limit the maximum number of participants to return
     * @return the list of top participants
     * @throws IllegalArgumentException if the tournament is not found
     */
    List<TournamentParticipantDto> getTopTournamentParticipants(UUID tournamentId, int limit);
    
    /**
     * Check if a user is registered for a tournament.
     *
     * @param tournamentId the tournament ID
     * @param userId the user ID
     * @return true if the user is registered, false otherwise
     * @throws IllegalArgumentException if the tournament is not found
     */
    boolean isUserRegisteredForTournament(UUID tournamentId, UUID userId);
    
    /**
     * Get a user's tournament history.
     *
     * @param userId the user ID
     * @param pageable the pagination information
     * @return a page of tournaments
     */
    Page<TournamentDto> getUserTournamentHistory(UUID userId, Pageable pageable);
    
    /**
     * Start a tournament.
     *
     * @param tournamentId the tournament ID
     * @return the updated tournament
     * @throws IllegalArgumentException if the tournament is not found
     * @throws IllegalStateException if the tournament cannot be started
     */
    Tournament startTournament(UUID tournamentId);
    
    /**
     * End a tournament.
     *
     * @param tournamentId the tournament ID
     * @return the updated tournament
     * @throws IllegalArgumentException if the tournament is not found
     * @throws IllegalStateException if the tournament cannot be ended
     */
    Tournament endTournament(UUID tournamentId);
    
    /**
     * Cancel a tournament.
     *
     * @param tournamentId the tournament ID
     * @return the updated tournament
     * @throws IllegalArgumentException if the tournament is not found
     * @throws IllegalStateException if the tournament cannot be cancelled
     */
    Tournament cancelTournament(UUID tournamentId);
    
    /**
     * Update a participant's score.
     *
     * @param tournamentId the tournament ID
     * @param userId the user ID
     * @param score the new score
     * @return the updated participant
     * @throws IllegalArgumentException if the tournament is not found or the user is not registered
     */
    TournamentParticipant updateParticipantScore(UUID tournamentId, UUID userId, long score);
    
    /**
     * Eliminate a participant from a tournament.
     *
     * @param tournamentId the tournament ID
     * @param userId the user ID
     * @return the updated participant
     * @throws IllegalArgumentException if the tournament is not found or the user is not registered
     * @throws IllegalStateException if the tournament is not in progress
     */
    TournamentParticipant eliminateParticipant(UUID tournamentId, UUID userId);
    
    /**
     * Convert a Tournament to a TournamentDto.
     *
     * @param tournament the tournament
     * @param currentUserId the current user ID (to check if the user is registered)
     * @return the tournament DTO
     */
    TournamentDto convertToDto(Tournament tournament, UUID currentUserId);
    
    /**
     * Convert a TournamentParticipant to a TournamentParticipantDto.
     *
     * @param participant the participant
     * @return the participant DTO
     */
    TournamentParticipantDto convertToDto(TournamentParticipant participant);
}
