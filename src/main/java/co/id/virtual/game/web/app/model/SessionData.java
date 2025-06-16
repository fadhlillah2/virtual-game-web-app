package co.id.virtual.game.web.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Class representing the data of a game session.
 * This will be stored as JSON in the database.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Current state of the game.
     */
    private String gameState;
    
    /**
     * Current round number.
     */
    private int roundNumber;
    
    /**
     * Current turn (player ID).
     */
    private UUID currentTurn;
    
    /**
     * List of player IDs in the session.
     */
    @Builder.Default
    private List<UUID> players = new ArrayList<>();
    
    /**
     * Map of player IDs to their current chip amounts in this session.
     */
    @Builder.Default
    private Map<UUID, Long> playerChips = new HashMap<>();
    
    /**
     * Map of player IDs to their current status in the game.
     */
    @Builder.Default
    private Map<UUID, String> playerStatus = new HashMap<>();
    
    /**
     * Game-specific data (depends on the game type).
     */
    @Builder.Default
    private Map<String, Object> gameData = new HashMap<>();
    
    /**
     * Timestamp of the last action.
     */
    private LocalDateTime lastActionTime;
    
    /**
     * Total pot size (for poker and similar games).
     */
    private Long potSize;
    
    /**
     * History of actions in the current round.
     */
    @Builder.Default
    private List<Map<String, Object>> actionHistory = new ArrayList<>();
}
