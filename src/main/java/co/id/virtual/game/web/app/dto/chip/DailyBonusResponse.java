package co.id.virtual.game.web.app.dto.chip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for representing the response when a user claims their daily bonus.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyBonusResponse {
    
    /**
     * The user's ID.
     */
    private UUID userId;
    
    /**
     * The amount of chips awarded as a bonus.
     */
    private Long bonusAmount;
    
    /**
     * The user's chip balance after receiving the bonus.
     */
    private Long newBalance;
    
    /**
     * The timestamp when the bonus was claimed.
     */
    private LocalDateTime claimedAt;
    
    /**
     * The timestamp when the next bonus can be claimed.
     */
    private LocalDateTime nextBonusAvailableAt;
    
    /**
     * Any streak or consecutive day bonus information.
     */
    private Integer consecutiveDays;
    
    /**
     * Any bonus multiplier applied.
     */
    private Double bonusMultiplier;
}
