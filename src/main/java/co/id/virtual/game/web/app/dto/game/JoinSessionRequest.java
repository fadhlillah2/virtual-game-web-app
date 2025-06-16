package co.id.virtual.game.web.app.dto.game;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for representing a request to join a game session.
 * This can be used to join an existing session or create a new one.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinSessionRequest {

    /**
     * The ID of the session to join.
     * This is optional when creating a new session.
     */
    private UUID sessionId;

    /**
     * The ID of the game to create a session for.
     * This is required when sessionId is not provided.
     */
    private UUID gameId;

    /**
     * The amount of chips to buy in with.
     */
    @NotNull(message = "Buy-in amount is required")
    @Min(value = 10, message = "Buy-in amount must be at least 10")
    private Long buyIn;
}
