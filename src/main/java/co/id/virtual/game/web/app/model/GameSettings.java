package co.id.virtual.game.web.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * Class representing the settings of a game.
 * This will be stored as JSON in the database.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameSettings implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Game-specific rules and settings.
     */
    private Map<String, Object> rules;
    
    /**
     * Payout configuration for the game.
     */
    private Map<String, Double> payouts;
    
    /**
     * Visual theme settings for the game.
     */
    private Map<String, String> theme;
    
    /**
     * Difficulty level settings.
     */
    private String difficulty;
    
    /**
     * Whether the game is available for tournaments.
     */
    private boolean tournamentEnabled;
    
    /**
     * Minimum level required to play this game.
     */
    private int minimumLevel;
    
    /**
     * Whether the game is available for premium users only.
     */
    private boolean premiumOnly;
}
