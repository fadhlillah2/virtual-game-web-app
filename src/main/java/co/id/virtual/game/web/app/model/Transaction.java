package co.id.virtual.game.web.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a transaction in the system.
 * Contains information about chip transactions, including daily bonuses,
 * game wins/losses, gifts, tournament entries/prizes, etc.
 */
@Entity
@Table(name = "transactions")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
    
    @Column(nullable = false)
    private Long amount;
    
    @Column(name = "balance_before", nullable = false)
    private Long balanceBefore;
    
    @Column(name = "balance_after", nullable = false)
    private Long balanceAfter;
    
    @Column(name = "reference_id")
    private UUID referenceId;
    
    private String description;
    
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
