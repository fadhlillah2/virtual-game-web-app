package co.id.virtual.game.web.app.event;

import co.id.virtual.game.web.app.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event that is published when a chip transaction occurs.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChipTransactionEvent {
    
    /**
     * The ID of the transaction.
     */
    private UUID transactionId;
    
    /**
     * The ID of the user.
     */
    private UUID userId;
    
    /**
     * The username of the user.
     */
    private String username;
    
    /**
     * The type of the transaction.
     */
    private TransactionType type;
    
    /**
     * The amount of chips involved in the transaction.
     */
    private Long amount;
    
    /**
     * The balance before the transaction.
     */
    private Long balanceBefore;
    
    /**
     * The balance after the transaction.
     */
    private Long balanceAfter;
    
    /**
     * The ID of the reference entity (e.g., game session ID for game-related transactions).
     */
    private UUID referenceId;
    
    /**
     * The description of the transaction.
     */
    private String description;
    
    /**
     * The time the transaction occurred.
     */
    private LocalDateTime transactionTime;
    
    /**
     * The ID of the recipient user, if applicable (e.g., for gift transactions).
     */
    private UUID recipientId;
    
    /**
     * The username of the recipient, if applicable.
     */
    private String recipientUsername;
    
    /**
     * Additional metadata about the transaction.
     */
    private String metadata;
}
