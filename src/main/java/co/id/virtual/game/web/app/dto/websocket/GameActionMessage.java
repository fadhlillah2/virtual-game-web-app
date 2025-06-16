package co.id.virtual.game.web.app.dto.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * DTO for representing a game action message sent from the client to the server.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameActionMessage {
    
    /**
     * The ID of the game session.
     */
    private UUID sessionId;
    
    /**
     * The type of action (e.g., "bet", "fold", "hit", "stand").
     */
    private String action;
    
    /**
     * The amount of chips involved in the action (if applicable).
     */
    private Long amount;
    
    /**
     * Additional parameters for the action.
     */
    private Map<String, Object> parameters;
}
