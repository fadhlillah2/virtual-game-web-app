package co.id.virtual.game.web.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing game-specific data for a user's game history.
 * This will be stored as JSON in the database.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Final position or rank in the game.
     */
    private Integer finalPosition;
    
    /**
     * Score achieved in the game.
     */
    private Integer score;
    
    /**
     * Game-specific statistics.
     */
    @Builder.Default
    private Map<String, Object> statistics = new HashMap<>();
    
    /**
     * List of significant actions taken during the game.
     */
    @Builder.Default
    private List<Map<String, Object>> actions = new ArrayList<>();
    
    /**
     * Game-specific achievements unlocked during this session.
     */
    @Builder.Default
    private List<String> achievements = new ArrayList<>();
    
    /**
     * Experience points earned in this game.
     */
    private Integer experiencePoints;
    
    /**
     * Additional metadata about the game.
     */
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();
}
