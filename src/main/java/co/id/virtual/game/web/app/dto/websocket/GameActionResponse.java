package co.id.virtual.game.web.app.dto.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for representing a game action response sent from the server to the client.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameActionResponse {
    
    /**
     * The ID of the game session.
     */
    private UUID sessionId;
    
    /**
     * The ID of the user who performed the action.
     */
    private UUID userId;
    
    /**
     * The username of the user who performed the action.
     */
    private String username;
    
    /**
     * The type of action that was performed.
     */
    private String action;
    
    /**
     * Whether the action was successful.
     */
    private boolean success;
    
    /**
     * An error message if the action was not successful.
     */
    private String errorMessage;
    
    /**
     * The amount of chips involved in the action (if applicable).
     */
    private Long amount;
    
    /**
     * The user's chip balance after the action.
     */
    private Long balanceAfter;
    
    /**
     * The timestamp when the action was processed.
     */
    private LocalDateTime timestamp;
    
    /**
     * Additional data about the action result.
     */
    private Map<String, Object> data;
    
    /**
     * The next action that should be taken (if applicable).
     */
    private String nextAction;
    
    /**
     * The ID of the user who should take the next action (if applicable).
     */
    private UUID nextUserId;
}
