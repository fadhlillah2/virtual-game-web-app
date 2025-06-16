package co.id.virtual.game.web.app.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Event that is published when a user performs an action in a game.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActionEvent {
    
    /**
     * The ID of the user.
     */
    private UUID userId;
    
    /**
     * The username of the user.
     */
    private String username;
    
    /**
     * The ID of the game.
     */
    private UUID gameId;
    
    /**
     * The ID of the game session.
     */
    private UUID sessionId;
    
    /**
     * The type of the game.
     */
    private String gameType;
    
    /**
     * The action performed by the user.
     */
    private String action;
    
    /**
     * The amount involved in the action, if applicable.
     */
    private Long amount;
    
    /**
     * The time the action was performed.
     */
    private LocalDateTime actionTime;
    
    /**
     * The round number in which the action was performed.
     */
    private Integer roundNumber;
    
    /**
     * The position of the user in the game.
     */
    private Integer position;
    
    /**
     * Additional parameters for the action.
     */
    private Map<String, Object> actionParams;
    
    /**
     * The result of the action.
     */
    private String result;
    
    /**
     * Additional metadata about the action.
     */
    private String metadata;
}
