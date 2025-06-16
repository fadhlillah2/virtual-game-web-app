package co.id.virtual.game.web.app.dto.chip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for representing the response when a user sends a gift to another user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftResponse {
    
    /**
     * The ID of the transaction.
     */
    private UUID transactionId;
    
    /**
     * The ID of the sender.
     */
    private UUID senderId;
    
    /**
     * The username of the sender.
     */
    private String senderUsername;
    
    /**
     * The ID of the recipient.
     */
    private UUID recipientId;
    
    /**
     * The username of the recipient.
     */
    private String recipientUsername;
    
    /**
     * The amount of chips sent.
     */
    private Long amount;
    
    /**
     * The sender's balance after sending the gift.
     */
    private Long senderBalanceAfter;
    
    /**
     * The recipient's balance after receiving the gift.
     */
    private Long recipientBalanceAfter;
    
    /**
     * The timestamp when the gift was sent.
     */
    private LocalDateTime sentAt;
    
    /**
     * The remaining gift limit for the day.
     */
    private Long remainingDailyGiftLimit;
}
