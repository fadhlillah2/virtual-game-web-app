package co.id.virtual.game.web.app.service;

import co.id.virtual.game.web.app.dto.leaderboard.LeaderboardEntryDto;
import co.id.virtual.game.web.app.model.GameType;

import java.util.List;

/**
 * Service for handling leaderboard operations.
 */
public interface LeaderboardService {
    
    /**
     * Get the global leaderboard.
     *
     * @param type the type of leaderboard (chips, wins, etc.)
     * @param limit the maximum number of entries to return
     * @return the leaderboard entries
     */
    List<LeaderboardEntryDto> getLeaderboard(String type, int limit);
    
    /**
     * Get the game-specific leaderboard.
     *
     * @param gameType the type of game
     * @param limit the maximum number of entries to return
     * @return the game-specific leaderboard entries
     */
    List<LeaderboardEntryDto> getGameLeaderboard(String gameType, int limit);
    
    /**
     * Get the user's rank on the global leaderboard.
     *
     * @param userId the user ID
     * @param type the type of leaderboard (chips, wins, etc.)
     * @return the user's leaderboard entry
     */
    LeaderboardEntryDto getUserRank(String userId, String type);
    
    /**
     * Get the user's rank on the game-specific leaderboard.
     *
     * @param userId the user ID
     * @param gameType the type of game
     * @return the user's game-specific leaderboard entry
     */
    LeaderboardEntryDto getUserGameRank(String userId, String gameType);
}
