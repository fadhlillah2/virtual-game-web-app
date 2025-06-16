package co.id.virtual.game.web.app.controller;

import co.id.virtual.game.web.app.dto.ApiResponse;
import co.id.virtual.game.web.app.dto.game.ActiveSessionDto;
import co.id.virtual.game.web.app.dto.game.JoinGameRequest;
import co.id.virtual.game.web.app.dto.game.JoinGameResponse;
import co.id.virtual.game.web.app.dto.game.JoinSessionRequest;
import co.id.virtual.game.web.app.dto.game.LeaveGameRequest;
import co.id.virtual.game.web.app.exception.InsufficientChipsException;
import co.id.virtual.game.web.app.model.Game;
import co.id.virtual.game.web.app.model.GameSession;
import co.id.virtual.game.web.app.model.SessionStatus;
import co.id.virtual.game.web.app.security.UserPrincipal;
import co.id.virtual.game.web.app.service.GameService;
import co.id.virtual.game.web.app.service.GameSessionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller for handling game session operations.
 */
@RestController
@RequestMapping("/api/games/sessions")
@PreAuthorize("hasRole('USER')")
@Slf4j
public class GameSessionController {
    
    private final GameSessionService gameSessionService;
    private final GameService gameService;
    
    @Autowired
    public GameSessionController(GameSessionService gameSessionService, GameService gameService) {
        this.gameSessionService = gameSessionService;
        this.gameService = gameService;
    }
    
    /**
     * Get all active game sessions.
     *
     * @return a list of active game sessions
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ActiveSessionDto>>> getActiveSessions() {
        List<GameSession> activeSessions = gameSessionService.findAllActiveSessions();
        List<ActiveSessionDto> sessionDtos = activeSessions.stream()
                .map(this::convertToActiveSessionDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success("Active sessions retrieved successfully", sessionDtos));
    }
    
    /**
     * Join a game by creating a new session or joining an existing one.
     *
     * @param request the join game request
     * @param userPrincipal the authenticated user
     * @return the join game response
     */
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<JoinGameResponse>> joinGame(
            @Valid @RequestBody JoinSessionRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        try {
            GameSession session;
            
            // If sessionId is provided, join that specific session
            if (request.getSessionId() != null) {
                session = gameSessionService.joinSession(request.getSessionId(), userPrincipal.getId(), request.getBuyIn());
            } else {
                // Otherwise, create a new session for the game
                session = gameSessionService.createSession(request.getGameId());
                session = gameSessionService.joinSession(session.getId(), userPrincipal.getId(), request.getBuyIn());
            }
            
            JoinGameResponse response = JoinGameResponse.builder()
                    .sessionId(session.getId())
                    .gameId(session.getGame().getId())
                    .gameName(session.getGame().getName())
                    .gameType(session.getGame().getType())
                    .buyIn(request.getBuyIn())
                    .build();
            
            log.info("User {} joined game session {}", userPrincipal.getUsername(), session.getId());
            
            return ResponseEntity.ok(ApiResponse.success("Joined game successfully", response));
        } catch (InsufficientChipsException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Insufficient chips"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error joining game", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to join game: " + e.getMessage()));
        }
    }
    
    /**
     * Leave a game session.
     *
     * @param request the leave game request
     * @param userPrincipal the authenticated user
     * @return a success response
     */
    @PostMapping("/leave")
    public ResponseEntity<ApiResponse<Void>> leaveGame(
            @Valid @RequestBody LeaveGameRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        try {
            gameSessionService.leaveSession(request.getSessionId(), userPrincipal.getId());
            
            log.info("User {} left game session {}", userPrincipal.getUsername(), request.getSessionId());
            
            return ResponseEntity.ok(ApiResponse.success("Left game successfully"));
        } catch (Exception e) {
            log.error("Error leaving game", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to leave game: " + e.getMessage()));
        }
    }
    
    /**
     * Convert a GameSession to an ActiveSessionDto.
     *
     * @param session the game session
     * @return the active session DTO
     */
    private ActiveSessionDto convertToActiveSessionDto(GameSession session) {
        Game game = session.getGame();
        int playerCount = session.getSessionData() != null && session.getSessionData().getPlayers() != null 
                ? session.getSessionData().getPlayers().size() 
                : 0;
        
        return ActiveSessionDto.builder()
                .id(session.getId())
                .gameId(game.getId())
                .gameName(game.getName())
                .gameType(game.getType())
                .playerCount(playerCount)
                .maxPlayers(game.getMaxPlayers())
                .minBuyIn(game.getMinBet())
                .potSize(session.getSessionData() != null ? session.getSessionData().getPotSize() : 0L)
                .startedAt(session.getStartedAt())
                .active(session.getStatus() == SessionStatus.ACTIVE)
                .build();
    }
}
