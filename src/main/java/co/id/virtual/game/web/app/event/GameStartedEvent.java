package co.id.virtual.game.web.app.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Event that is published when a game starts.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameStartedEvent {
    
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
     * The list of player IDs.
     */
    private List<UUID> playerIds;
    
    /**
     * The time the game started.
     */
    private LocalDateTime startTime;
    
    /**
     * The initial pot size.
     */
    private Long initialPotSize;
    
    /**
     * Additional metadata about the game.
     */
    private String metadata;
}
