package co.id.virtual.game.web.app.dto.chip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for representing a user's chip balance.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChipBalanceDto {
    
    /**
     * The user's ID.
     */
    private UUID userId;
    
    /**
     * The user's current chip balance.
     */
    private Long chipsBalance;
    
    /**
     * The total number of chips the user has won.
     */
    private Long totalChipsWon;
    
    /**
     * The timestamp of the last chip transaction.
     */
    private LocalDateTime lastTransactionTime;
    
    /**
     * Whether the user has claimed their daily bonus today.
     */
    private Boolean dailyBonusClaimed;
    
    /**
     * The timestamp when the daily bonus was last claimed.
     */
    private LocalDateTime lastDailyBonusTime;
}
