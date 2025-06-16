package co.id.virtual.game.web.app.dto.tournament;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for tournament participant information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentParticipantDto {
    
    /**
     * The participant's user ID.
     */
    private String userId;
    
    /**
     * The participant's username.
     */
    private String username;
    
    /**
     * The participant's avatar URL.
     */
    private String avatarUrl;
    
    /**
     * The participant's current score.
     */
    private long score;
    
    /**
     * The participant's current rank.
     */
    private int rank;
    
    /**
     * Whether the participant is eliminated.
     */
    private boolean eliminated;
    
    /**
     * When the participant registered for the tournament.
     */
    private LocalDateTime registeredAt;
    
    /**
     * When the participant was eliminated (if applicable).
     */
    private LocalDateTime eliminatedAt;
}
