package co.id.virtual.game.web.app.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Event that is published when a game ends.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameEndedEvent {
    
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
     * The time the game started.
     */
    private LocalDateTime startTime;
    
    /**
     * The time the game ended.
     */
    private LocalDateTime endTime;
    
    /**
     * The duration of the game in seconds.
     */
    private Long durationSeconds;
    
    /**
     * The final pot size.
     */
    private Long finalPotSize;
    
    /**
     * Map of player IDs to their final chip amounts.
     */
    private Map<UUID, Long> playerChips;
    
    /**
     * The ID of the winner, if applicable.
     */
    private UUID winnerId;
    
    /**
     * The amount won by the winner.
     */
    private Long winAmount;
    
    /**
     * The reason the game ended.
     */
    private String endReason;
    
    /**
     * Additional metadata about the game.
     */
    private String metadata;
}
