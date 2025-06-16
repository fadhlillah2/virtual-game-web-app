package co.id.virtual.game.web.app.dto.game;

import co.id.virtual.game.web.app.model.GameType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for representing a response to a join game request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinGameResponse {

    /**
     * The ID of the game session that was joined.
     */
    private UUID sessionId;

    /**
     * The ID of the game that was joined.
     */
    private UUID gameId;

    /**
     * The name of the game that was joined.
     */
    private String gameName;

    /**
     * The type of the game that was joined.
     */
    private GameType gameType;

    /**
     * The amount of chips that were used to buy in.
     */
    private Long buyIn;

    /**
     * The user's remaining chip balance after buying in.
     */
    private Long remainingBalance;
}
