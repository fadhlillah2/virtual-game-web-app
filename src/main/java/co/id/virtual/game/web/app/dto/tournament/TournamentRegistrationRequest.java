package co.id.virtual.game.web.app.dto.tournament;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for tournament registration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentRegistrationRequest {
    
    /**
     * The tournament ID.
     */
    @NotBlank(message = "Tournament ID is required")
    private String tournamentId;
}
