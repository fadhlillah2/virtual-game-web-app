package co.id.virtual.game.web.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a tournament.
 */
@Entity
@Table(name = "tournaments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {
    
    /**
     * The tournament ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    /**
     * The tournament name.
     */
    @Column(nullable = false)
    private String name;
    
    /**
     * The tournament description.
     */
    @Column(length = 1000)
    private String description;
    
    /**
     * The game type.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameType gameType;
    
    /**
     * The entry fee (in chips).
     */
    @Column(nullable = false)
    private int entryFee;
    
    /**
     * The prize pool (in chips).
     */
    @Column(nullable = false)
    private long prizePool;
    
    /**
     * The maximum number of participants.
     */
    @Column(nullable = false)
    private int maxParticipants;
    
    /**
     * The tournament status.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TournamentStatus status;
    
    /**
     * The start time.
     */
    @Column(nullable = false)
    private LocalDateTime startTime;
    
    /**
     * The end time.
     */
    private LocalDateTime endTime;
    
    /**
     * The registration deadline.
     */
    @Column(nullable = false)
    private LocalDateTime registrationDeadline;
    
    /**
     * The tournament rules.
     */
    @Column(length = 2000)
    private String rules;
    
    /**
     * The prize distribution (JSON).
     */
    @Column(columnDefinition = "json")
    @Convert(converter = JsonType.class)
    private Object prizeDistribution;
    
    /**
     * When the tournament was created.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * When the tournament was last updated.
     */
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
