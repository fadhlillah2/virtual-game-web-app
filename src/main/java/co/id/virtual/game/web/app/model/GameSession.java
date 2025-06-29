package co.id.virtual.game.web.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity representing a game session in the system.
 * Contains information about a specific instance of a game being played.
 */
@Entity
@Table(name = "game_sessions")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;
    
    @Convert(converter = JsonAttributeConverter.class)
    @Column(name = "session_data", columnDefinition = "jsonb")
    private SessionData sessionData;
    
    @Enumerated(EnumType.STRING)
    private SessionStatus status = SessionStatus.ACTIVE;
    
    @Column(name = "started_at")
    private LocalDateTime startedAt = LocalDateTime.now();
    
    @Column(name = "ended_at")
    private LocalDateTime endedAt;
    
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @Builder.Default
    private List<UserGameHistory> gameHistory = new ArrayList<>();
    
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
