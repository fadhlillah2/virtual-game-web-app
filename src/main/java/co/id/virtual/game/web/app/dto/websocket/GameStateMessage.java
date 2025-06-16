package co.id.virtual.game.web.app.dto.websocket;

import co.id.virtual.game.web.app.model.GameType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for representing the current state of a game.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameStateMessage {
    
    /**
     * The ID of the game.
     */
    private UUID gameId;
    
    /**
     * The ID of the game session.
     */
    private UUID sessionId;
    
    /**
     * The name of the game.
     */
    private String gameName;
    
    /**
     * The type of the game.
     */
    private GameType gameType;
    
    /**
     * The current state of the game (e.g., "waiting", "playing", "ended").
     */
    private String gameState;
    
    /**
     * The current round number.
     */
    private Integer roundNumber;
    
    /**
     * The ID of the user whose turn it is.
     */
    private UUID currentTurn;
    
    /**
     * The list of player IDs in the game.
     */
    private List<UUID> players;
    
    /**
     * Map of player IDs to their usernames.
     */
    private Map<UUID, String> playerNames;
    
    /**
     * Map of player IDs to their current chip amounts in this session.
     */
    private Map<UUID, Long> playerChips;
    
    /**
     * Map of player IDs to their current status in the game.
     */
    private Map<UUID, String> playerStatus;
    
    /**
     * The timestamp of the last action.
     */
    private LocalDateTime lastActionTime;
    
    /**
     * The total pot size (for poker and similar games).
     */
    private Long potSize;
    
    /**
     * Game-specific data (depends on the game type).
     */
    private Map<String, Object> gameData;
    
    /**
     * History of actions in the current round.
     */
    private List<Map<String, Object>> actionHistory;
    
    /**
     * The timestamp when the message was created.
     */
    private LocalDateTime timestamp;
    
    /**
     * Whether the game is active.
     */
    private boolean active;
    
    /**
     * The minimum bet amount.
     */
    private Integer minBet;
    
    /**
     * The maximum bet amount.
     */
    private Integer maxBet;
}
