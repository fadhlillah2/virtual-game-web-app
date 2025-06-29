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
 * Entity representing a game in the system.
 * Contains information about a specific game type, its rules, and settings.
 */
@Entity
@Table(name = "games")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameType type;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private GameStatus status = GameStatus.ACTIVE;
    
    @Column(name = "min_bet")
    private Integer minBet = 10;
    
    @Column(name = "max_bet")
    private Integer maxBet = 1000;
    
    @Column(name = "max_players")
    private Integer maxPlayers = 6;
    
    @Convert(converter = JsonAttributeConverter.class)
    @Column(columnDefinition = "jsonb")
    private GameSettings settings;
    
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
