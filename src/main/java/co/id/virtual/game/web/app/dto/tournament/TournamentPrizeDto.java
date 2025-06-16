package co.id.virtual.game.web.app.dto.tournament;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for tournament prize information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentPrizeDto {
    
    /**
     * The prize position (1st, 2nd, 3rd, etc.).
     */
    private int position;
    
    /**
     * The prize amount (in chips).
     */
    private long amount;
    
    /**
     * The percentage of the prize pool.
     */
    private double percentage;
    
    /**
     * The winner's user ID (if the tournament is completed).
     */
    private String winnerId;
    
    /**
     * The winner's username (if the tournament is completed).
     */
    private String winnerUsername;
}
