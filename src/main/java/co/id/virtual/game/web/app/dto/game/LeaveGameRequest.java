package co.id.virtual.game.web.app.dto.game;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for representing a request to leave a game session.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveGameRequest {
    
    /**
     * The ID of the session to leave.
     */
    @NotNull(message = "Session ID is required")
    private UUID sessionId;
}
