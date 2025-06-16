package co.id.virtual.game.web.app.dto.leaderboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for leaderboard entries.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardEntryDto {
    
    /**
     * The user ID.
     */
    private String id;
    
    /**
     * The username.
     */
    private String username;
    
    /**
     * The user's avatar URL.
     */
    private String avatarUrl;
    
    /**
     * The user's rank on the leaderboard.
     */
    private int rank;
    
    /**
     * The user's score (chips, wins, etc.).
     */
    private long score;
    
    /**
     * The number of games played.
     */
    private int gamesPlayed;
    
    /**
     * The number of games won.
     */
    private int gamesWon;
    
    /**
     * The win rate (percentage).
     */
    private double winRate;
}
