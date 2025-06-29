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
 * Entity representing a tournament participant.
 */
@Entity
@Table(name = "tournament_participants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentParticipant {
    
    /**
     * The participant ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    /**
     * The tournament.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;
    
    /**
     * The user.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    /**
     * The participant's current score.
     */
    @Column(nullable = false)
    private long score;
    
    /**
     * The participant's current rank.
     */
    private Integer rank;
    
    /**
     * Whether the participant is eliminated.
     */
    @Column(nullable = false)
    private boolean eliminated;
    
    /**
     * When the participant was eliminated (if applicable).
     */
    private LocalDateTime eliminatedAt;
    
    /**
     * Additional participant data (JSON).
     */
    @Column(columnDefinition = "json")
    @Convert(converter = JsonAttributeConverter.class)
    private Object participantData;
    
    /**
     * When the participant registered for the tournament.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * When the participant was last updated.
     */
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
