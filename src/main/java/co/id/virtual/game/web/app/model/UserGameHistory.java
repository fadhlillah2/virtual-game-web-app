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
 * Entity representing a user's history of playing a game.
 * Contains information about a specific game session for a user.
 */
@Entity
@Table(name = "user_game_history")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGameHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private GameSession session;
    
    @Column(name = "chips_before", nullable = false)
    private Long chipsBefore;
    
    @Column(name = "chips_after", nullable = false)
    private Long chipsAfter;
    
    @Column(name = "chips_wagered", nullable = false)
    private Long chipsWagered;
    
    @Column(name = "chips_won")
    private Long chipsWon = 0L;
    
    @Convert(converter = JsonAttributeConverter.class)
    @Column(name = "game_data", columnDefinition = "jsonb")
    private GameData gameData;
    
    @Column(name = "played_at")
    private LocalDateTime playedAt = LocalDateTime.now();
    
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
