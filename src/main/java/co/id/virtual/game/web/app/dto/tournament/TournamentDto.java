package co.id.virtual.game.web.app.dto.tournament;

import co.id.virtual.game.web.app.model.GameType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for tournament information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentDto {
    
    /**
     * The tournament ID.
     */
    private String id;
    
    /**
     * The tournament name.
     */
    private String name;
    
    /**
     * The tournament description.
     */
    private String description;
    
    /**
     * The game type.
     */
    private GameType gameType;
    
    /**
     * The entry fee (in chips).
     */
    private int entryFee;
    
    /**
     * The prize pool (in chips).
     */
    private long prizePool;
    
    /**
     * The maximum number of participants.
     */
    private int maxParticipants;
    
    /**
     * The current number of participants.
     */
    private int currentParticipants;
    
    /**
     * The tournament status.
     */
    private String status;
    
    /**
     * The start time.
     */
    private LocalDateTime startTime;
    
    /**
     * The end time.
     */
    private LocalDateTime endTime;
    
    /**
     * The registration deadline.
     */
    private LocalDateTime registrationDeadline;
    
    /**
     * Whether the current user is registered for the tournament.
     */
    private boolean isRegistered;
    
    /**
     * The list of prizes.
     */
    private List<TournamentPrizeDto> prizes;
}
