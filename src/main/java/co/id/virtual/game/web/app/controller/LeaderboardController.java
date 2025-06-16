package co.id.virtual.game.web.app.controller;

import co.id.virtual.game.web.app.dto.ApiResponse;
import co.id.virtual.game.web.app.dto.leaderboard.LeaderboardEntryDto;
import co.id.virtual.game.web.app.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Controller for handling leaderboard operations.
 */
@Controller
@RequestMapping("/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    /**
     * Display the leaderboard page.
     *
     * @return the leaderboard view
     */
    @GetMapping
    public ModelAndView showLeaderboard() {
        return new ModelAndView("leaderboard");
    }

    /**
     * Get the global leaderboard data.
     *
     * @param type the type of leaderboard (chips, wins, etc.)
     * @param limit the maximum number of entries to return
     * @return the leaderboard data
     */
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<LeaderboardEntryDto>>> getLeaderboard(
            @RequestParam(defaultValue = "chips") String type,
            @RequestParam(defaultValue = "10") int limit) {

        List<LeaderboardEntryDto> leaderboard = leaderboardService.getLeaderboard(type, limit);
        return ResponseEntity.ok(ApiResponse.success("Leaderboard retrieved successfully", leaderboard));
    }

    /**
     * Get game-specific leaderboard data.
     *
     * @param gameType the type of game (POKER, BLACKJACK, etc.)
     * @param limit the maximum number of entries to return
     * @return the game-specific leaderboard data
     */
    @GetMapping("/api/game")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<LeaderboardEntryDto>>> getGameLeaderboard(
            @RequestParam String gameType,
            @RequestParam(defaultValue = "10") int limit) {

        List<LeaderboardEntryDto> leaderboard = leaderboardService.getGameLeaderboard(gameType, limit);
        return ResponseEntity.ok(ApiResponse.success("Game leaderboard retrieved successfully", leaderboard));
    }
}
