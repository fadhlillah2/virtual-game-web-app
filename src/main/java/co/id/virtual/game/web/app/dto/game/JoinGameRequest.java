package co.id.virtual.game.web.app.dto.game;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for representing a request to join a game.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinGameRequest {
    
    /**
     * The ID of the game to join.
     */
    @NotNull(message = "Game ID is required")
    private UUID gameId;
    
    /**
     * The amount of chips to buy in with.
     */
    @NotNull(message = "Buy-in amount is required")
    @Min(value = 10, message = "Buy-in amount must be at least 10")
    private Long buyIn;
}
