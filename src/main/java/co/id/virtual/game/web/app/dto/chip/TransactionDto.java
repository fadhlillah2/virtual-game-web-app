package co.id.virtual.game.web.app.dto.chip;

import co.id.virtual.game.web.app.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for representing a transaction in the system.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    
    /**
     * The ID of the transaction.
     */
    private UUID id;
    
    /**
     * The ID of the user.
     */
    private UUID userId;
    
    /**
     * The username of the user.
     */
    private String username;
    
    /**
     * The type of transaction.
     */
    private TransactionType type;
    
    /**
     * The amount of chips involved in the transaction.
     */
    private Long amount;
    
    /**
     * The user's balance before the transaction.
     */
    private Long balanceBefore;
    
    /**
     * The user's balance after the transaction.
     */
    private Long balanceAfter;
    
    /**
     * The ID of the reference entity (e.g., game session, gift recipient).
     */
    private UUID referenceId;
    
    /**
     * A description of the transaction.
     */
    private String description;
    
    /**
     * The timestamp when the transaction was created.
     */
    private LocalDateTime createdAt;
}
