package co.id.virtual.game.web.app.dto.game;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for representing a request to join an existing game session.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinSessionRequest {
    
    /**
     * The ID of the session to join.
     */
    @NotNull(message = "Session ID is required")
    private UUID sessionId;
    
    /**
     * The amount of chips to buy in with.
     */
    @NotNull(message = "Buy-in amount is required")
    @Min(value = 10, message = "Buy-in amount must be at least 10")
    private Long buyIn;
}
