package co.id.virtual.game.web.app.controller;

import co.id.virtual.game.web.app.dto.ApiResponse;
import co.id.virtual.game.web.app.dto.tournament.TournamentDto;
import co.id.virtual.game.web.app.dto.tournament.TournamentParticipantDto;
import co.id.virtual.game.web.app.dto.tournament.TournamentRegistrationRequest;
import co.id.virtual.game.web.app.model.GameType;
import co.id.virtual.game.web.app.model.Tournament;
import co.id.virtual.game.web.app.model.TournamentParticipant;
import co.id.virtual.game.web.app.model.TournamentStatus;
import co.id.virtual.game.web.app.security.UserPrincipal;
import co.id.virtual.game.web.app.service.TournamentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

/**
 * Controller for handling tournament operations.
 */
@Controller
@RequestMapping("/tournaments")
@Slf4j
public class TournamentController {
    
    private final TournamentService tournamentService;
    
    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }
    
    /**
     * Display the tournaments page.
     *
     * @return the tournaments view
     */
    @GetMapping
    public ModelAndView showTournamentsPage() {
        return new ModelAndView("tournaments");
    }
    
    /**
     * Display a specific tournament page.
     *
     * @param tournamentId the tournament ID
     * @return the tournament view
     */
    @GetMapping("/{tournamentId}")
    public ModelAndView showTournamentPage(@PathVariable String tournamentId) {
        ModelAndView modelAndView = new ModelAndView("tournament-details");
        modelAndView.addObject("tournamentId", tournamentId);
        return modelAndView;
    }
    
    /**
     * Get all tournaments.
     *
     * @param page the page number
     * @param size the page size
     * @return a page of tournaments
     */
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<ApiResponse<Page<TournamentDto>>> getAllTournaments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").descending());
        Page<TournamentDto> tournaments = tournamentService.getAllTournaments(pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Tournaments retrieved successfully", tournaments));
    }
    
    /**
     * Get a tournament by ID.
     *
     * @param tournamentId the tournament ID
     * @param userPrincipal the authenticated user
     * @return the tournament
     */
    @GetMapping("/api/{tournamentId}")
    @ResponseBody
    public ResponseEntity<ApiResponse<TournamentDto>> getTournamentById(
            @PathVariable String tournamentId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        try {
            UUID tournamentUuid = UUID.fromString(tournamentId);
            TournamentDto tournament = tournamentService.getTournamentById(tournamentUuid);
            
            return ResponseEntity.ok(ApiResponse.success("Tournament retrieved successfully", tournament));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get tournaments by status.
     *
     * @param status the tournament status
     * @return the list of tournaments
     */
    @GetMapping("/api/status/{status}")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<TournamentDto>>> getTournamentsByStatus(
            @PathVariable TournamentStatus status) {
        
        List<TournamentDto> tournaments = tournamentService.getTournamentsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Tournaments retrieved successfully", tournaments));
    }
    
    /**
     * Get tournaments by game type.
     *
     * @param gameType the game type
     * @return the list of tournaments
     */
    @GetMapping("/api/game-type/{gameType}")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<TournamentDto>>> getTournamentsByGameType(
            @PathVariable GameType gameType) {
        
        List<TournamentDto> tournaments = tournamentService.getTournamentsByGameType(gameType);
        return ResponseEntity.ok(ApiResponse.success("Tournaments retrieved successfully", tournaments));
    }
    
    /**
     * Get upcoming tournaments.
     *
     * @return the list of upcoming tournaments
     */
    @GetMapping("/api/upcoming")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<TournamentDto>>> getUpcomingTournaments() {
        List<TournamentDto> tournaments = tournamentService.getUpcomingTournaments();
        return ResponseEntity.ok(ApiResponse.success("Upcoming tournaments retrieved successfully", tournaments));
    }
    
    /**
     * Get active tournaments.
     *
     * @return the list of active tournaments
     */
    @GetMapping("/api/active")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<TournamentDto>>> getActiveTournaments() {
        List<TournamentDto> tournaments = tournamentService.getActiveTournaments();
        return ResponseEntity.ok(ApiResponse.success("Active tournaments retrieved successfully", tournaments));
    }
    
    /**
     * Get completed tournaments.
     *
     * @param page the page number
     * @param size the page size
     * @return a page of completed tournaments
     */
    @GetMapping("/api/completed")
    @ResponseBody
    public ResponseEntity<ApiResponse<Page<TournamentDto>>> getCompletedTournaments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("endTime").descending());
        Page<TournamentDto> tournaments = tournamentService.getCompletedTournaments(pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Completed tournaments retrieved successfully", tournaments));
    }
    
    /**
     * Register for a tournament.
     *
     * @param request the registration request
     * @param userPrincipal the authenticated user
     * @return a success response
     */
    @PostMapping("/api/register")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> registerForTournament(
            @Valid @RequestBody TournamentRegistrationRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        try {
            UUID tournamentId = UUID.fromString(request.getTournamentId());
            tournamentService.registerForTournament(tournamentId, userPrincipal.getId());
            
            return ResponseEntity.ok(ApiResponse.success("Registered for tournament successfully"));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Unregister from a tournament.
     *
     * @param tournamentId the tournament ID
     * @param userPrincipal the authenticated user
     * @return a success response
     */
    @DeleteMapping("/api/{tournamentId}/register")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> unregisterFromTournament(
            @PathVariable String tournamentId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        try {
            UUID tournamentUuid = UUID.fromString(tournamentId);
            tournamentService.unregisterFromTournament(tournamentUuid, userPrincipal.getId());
            
            return ResponseEntity.ok(ApiResponse.success("Unregistered from tournament successfully"));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get the participants of a tournament.
     *
     * @param tournamentId the tournament ID
     * @return the list of participants
     */
    @GetMapping("/api/{tournamentId}/participants")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<TournamentParticipantDto>>> getTournamentParticipants(
            @PathVariable String tournamentId) {
        
        try {
            UUID tournamentUuid = UUID.fromString(tournamentId);
            List<TournamentParticipantDto> participants = tournamentService.getTournamentParticipants(tournamentUuid);
            
            return ResponseEntity.ok(ApiResponse.success("Tournament participants retrieved successfully", participants));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get the top participants of a tournament.
     *
     * @param tournamentId the tournament ID
     * @param limit the maximum number of participants to return
     * @return the list of top participants
     */
    @GetMapping("/api/{tournamentId}/top-participants")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<TournamentParticipantDto>>> getTopTournamentParticipants(
            @PathVariable String tournamentId,
            @RequestParam(defaultValue = "10") int limit) {
        
        try {
            UUID tournamentUuid = UUID.fromString(tournamentId);
            List<TournamentParticipantDto> participants = tournamentService.getTopTournamentParticipants(tournamentUuid, limit);
            
            return ResponseEntity.ok(ApiResponse.success("Top tournament participants retrieved successfully", participants));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Check if a user is registered for a tournament.
     *
     * @param tournamentId the tournament ID
     * @param userPrincipal the authenticated user
     * @return whether the user is registered
     */
    @GetMapping("/api/{tournamentId}/is-registered")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Boolean>> isUserRegisteredForTournament(
            @PathVariable String tournamentId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        try {
            UUID tournamentUuid = UUID.fromString(tournamentId);
            boolean isRegistered = tournamentService.isUserRegisteredForTournament(tournamentUuid, userPrincipal.getId());
            
            return ResponseEntity.ok(ApiResponse.success("Registration status checked", isRegistered));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get a user's tournament history.
     *
     * @param userPrincipal the authenticated user
     * @param page the page number
     * @param size the page size
     * @return a page of tournaments
     */
    @GetMapping("/api/history")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Page<TournamentDto>>> getUserTournamentHistory(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").descending());
        Page<TournamentDto> tournaments = tournamentService.getUserTournamentHistory(userPrincipal.getId(), pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Tournament history retrieved successfully", tournaments));
    }
    
    /**
     * Create a new tournament (admin only).
     *
     * @param tournament the tournament to create
     * @return the created tournament
     */
    @PostMapping("/api/admin")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Tournament>> createTournament(
            @Valid @RequestBody Tournament tournament) {
        
        try {
            Tournament createdTournament = tournamentService.createTournament(tournament);
            return ResponseEntity.ok(ApiResponse.success("Tournament created successfully", createdTournament));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Update a tournament (admin only).
     *
     * @param tournamentId the tournament ID
     * @param tournament the updated tournament
     * @return the updated tournament
     */
    @PutMapping("/api/admin/{tournamentId}")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Tournament>> updateTournament(
            @PathVariable String tournamentId,
            @Valid @RequestBody Tournament tournament) {
        
        try {
            UUID tournamentUuid = UUID.fromString(tournamentId);
            Tournament updatedTournament = tournamentService.updateTournament(tournamentUuid, tournament);
            
            return ResponseEntity.ok(ApiResponse.success("Tournament updated successfully", updatedTournament));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Delete a tournament (admin only).
     *
     * @param tournamentId the tournament ID
     * @return a success response
     */
    @DeleteMapping("/api/admin/{tournamentId}")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTournament(
            @PathVariable String tournamentId) {
        
        try {
            UUID tournamentUuid = UUID.fromString(tournamentId);
            tournamentService.deleteTournament(tournamentUuid);
            
            return ResponseEntity.ok(ApiResponse.success("Tournament deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Start a tournament (admin only).
     *
     * @param tournamentId the tournament ID
     * @return the updated tournament
     */
    @PostMapping("/api/admin/{tournamentId}/start")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Tournament>> startTournament(
            @PathVariable String tournamentId) {
        
        try {
            UUID tournamentUuid = UUID.fromString(tournamentId);
            Tournament tournament = tournamentService.startTournament(tournamentUuid);
            
            return ResponseEntity.ok(ApiResponse.success("Tournament started successfully", tournament));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * End a tournament (admin only).
     *
     * @param tournamentId the tournament ID
     * @return the updated tournament
     */
    @PostMapping("/api/admin/{tournamentId}/end")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Tournament>> endTournament(
            @PathVariable String tournamentId) {
        
        try {
            UUID tournamentUuid = UUID.fromString(tournamentId);
            Tournament tournament = tournamentService.endTournament(tournamentUuid);
            
            return ResponseEntity.ok(ApiResponse.success("Tournament ended successfully", tournament));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Cancel a tournament (admin only).
     *
     * @param tournamentId the tournament ID
     * @return the updated tournament
     */
    @PostMapping("/api/admin/{tournamentId}/cancel")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Tournament>> cancelTournament(
            @PathVariable String tournamentId) {
        
        try {
            UUID tournamentUuid = UUID.fromString(tournamentId);
            Tournament tournament = tournamentService.cancelTournament(tournamentUuid);
            
            return ResponseEntity.ok(ApiResponse.success("Tournament cancelled successfully", tournament));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Update a participant's score (admin only).
     *
     * @param tournamentId the tournament ID
     * @param userId the user ID
     * @param score the new score
     * @return the updated participant
     */
    @PostMapping("/api/admin/{tournamentId}/participants/{userId}/score")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TournamentParticipant>> updateParticipantScore(
            @PathVariable String tournamentId,
            @PathVariable String userId,
            @RequestParam long score) {
        
        try {
            UUID tournamentUuid = UUID.fromString(tournamentId);
            UUID userUuid = UUID.fromString(userId);
            
            TournamentParticipant participant = tournamentService.updateParticipantScore(tournamentUuid, userUuid, score);
            
            return ResponseEntity.ok(ApiResponse.success("Participant score updated successfully", participant));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Eliminate a participant from a tournament (admin only).
     *
     * @param tournamentId the tournament ID
     * @param userId the user ID
     * @return the updated participant
     */
    @PostMapping("/api/admin/{tournamentId}/participants/{userId}/eliminate")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TournamentParticipant>> eliminateParticipant(
            @PathVariable String tournamentId,
            @PathVariable String userId) {
        
        try {
            UUID tournamentUuid = UUID.fromString(tournamentId);
            UUID userUuid = UUID.fromString(userId);
            
            TournamentParticipant participant = tournamentService.eliminateParticipant(tournamentUuid, userUuid);
            
            return ResponseEntity.ok(ApiResponse.success("Participant eliminated successfully", participant));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
