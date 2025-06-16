package co.id.virtual.game.web.app.dto.chip;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for representing a request to send a gift to another user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendGiftRequest {
    
    /**
     * The ID of the recipient.
     */
    @NotNull(message = "Recipient ID is required")
    private UUID recipientId;
    
    /**
     * The amount of chips to send.
     */
    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be at least 1")
    private Long amount;
    
    /**
     * An optional message to include with the gift.
     */
    private String message;
}
