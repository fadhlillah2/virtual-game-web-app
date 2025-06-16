package co.id.virtual.game.web.app.dto.game;

import co.id.virtual.game.web.app.model.GameType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for representing an active game session in the lobby.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActiveSessionDto {
    
    /**
     * The ID of the session.
     */
    private UUID id;
    
    /**
     * The ID of the game.
     */
    private UUID gameId;
    
    /**
     * The name of the game.
     */
    private String gameName;
    
    /**
     * The type of the game.
     */
    private GameType gameType;
    
    /**
     * The current number of players in the session.
     */
    private int playerCount;
    
    /**
     * The maximum number of players allowed in the session.
     */
    private int maxPlayers;
    
    /**
     * The minimum buy-in amount required to join the session.
     */
    private int minBuyIn;
    
    /**
     * The current pot size in the session.
     */
    private Long potSize;
    
    /**
     * The time when the session started.
     */
    private LocalDateTime startedAt;
    
    /**
     * Whether the session is active.
     */
    private boolean active;
}
