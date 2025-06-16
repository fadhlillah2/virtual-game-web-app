package co.id.virtual.game.web.app.controller;

import co.id.virtual.game.web.app.dto.ApiResponse;
import co.id.virtual.game.web.app.model.Game;
import co.id.virtual.game.web.app.model.GameStatus;
import co.id.virtual.game.web.app.model.GameType;
import co.id.virtual.game.web.app.model.UserGameHistory;
import co.id.virtual.game.web.app.security.UserPrincipal;
import co.id.virtual.game.web.app.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for handling game operations.
 */
@RestController
@RequestMapping("/api/games")
public class GameController {
    
    private final GameService gameService;
    
    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }
    
    /**
     * Get all active games.
     *
     * @return a list of all active games
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Game>>> getAllActiveGames() {
        List<Game> games = gameService.findAllActiveGames();
        return ResponseEntity.ok(ApiResponse.success("Active games retrieved successfully", games));
    }
    
    /**
     * Get a game by ID.
     *
     * @param id the game ID
     * @return the game
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Game>> getGameById(@PathVariable UUID id) {
        try {
            Game game = gameService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Game not found with ID: " + id));
            
            return ResponseEntity.ok(ApiResponse.success("Game retrieved successfully", game));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get games by type.
     *
     * @param type the game type
     * @return a list of games of the specified type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<Game>>> getGamesByType(@PathVariable GameType type) {
        List<Game> games = gameService.findByType(type);
        return ResponseEntity.ok(ApiResponse.success("Games retrieved successfully", games));
    }
    
    /**
     * Get active games by type.
     *
     * @param type the game type
     * @return a list of active games of the specified type
     */
    @GetMapping("/active/type/{type}")
    public ResponseEntity<ApiResponse<List<Game>>> getActiveGamesByType(@PathVariable GameType type) {
        List<Game> games = gameService.findActiveGamesByType(type);
        return ResponseEntity.ok(ApiResponse.success("Active games retrieved successfully", games));
    }
    
    /**
     * Get games by bet range.
     *
     * @param minBet the minimum bet amount
     * @param maxBet the maximum bet amount
     * @return a list of games with bet limits within the specified range
     */
    @GetMapping("/bet-range")
    public ResponseEntity<ApiResponse<List<Game>>> getGamesByBetRange(
            @RequestParam Integer minBet,
            @RequestParam Integer maxBet) {
        List<Game> games = gameService.findByBetRange(minBet, maxBet);
        return ResponseEntity.ok(ApiResponse.success("Games retrieved successfully", games));
    }
    
    /**
     * Get games by maximum number of players.
     *
     * @param players the number of players
     * @return a list of games that support the specified number of players
     */
    @GetMapping("/max-players/{players}")
    public ResponseEntity<ApiResponse<List<Game>>> getGamesByMaxPlayers(@PathVariable Integer players) {
        List<Game> games = gameService.findByMaxPlayers(players);
        return ResponseEntity.ok(ApiResponse.success("Games retrieved successfully", games));
    }
    
    /**
     * Get the current user's game history.
     *
     * @param userPrincipal the authenticated user
     * @param page the page number
     * @param size the page size
     * @return a page of the user's game history
     */
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<Page<UserGameHistory>>> getUserGameHistory(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("playedAt").descending());
        Page<UserGameHistory> history = gameService.getUserGameHistory(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success("Game history retrieved successfully", history));
    }
    
    /**
     * Get the current user's game history for a specific game.
     *
     * @param userPrincipal the authenticated user
     * @param gameId the game ID
     * @param page the page number
     * @param size the page size
     * @return a page of the user's game history for the specified game
     */
    @GetMapping("/history/{gameId}")
    public ResponseEntity<ApiResponse<Page<UserGameHistory>>> getUserGameHistoryByGame(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable UUID gameId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("playedAt").descending());
        Page<UserGameHistory> history = gameService.getUserGameHistoryByGame(userPrincipal.getId(), gameId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Game history retrieved successfully", history));
    }
    
    /**
     * Count the number of games by type.
     *
     * @param type the game type
     * @return the number of games of the specified type
     */
    @GetMapping("/count/type/{type}")
    public ResponseEntity<ApiResponse<Long>> countGamesByType(@PathVariable GameType type) {
        long count = gameService.countByType(type);
        return ResponseEntity.ok(ApiResponse.success("Game count retrieved successfully", count));
    }
}
