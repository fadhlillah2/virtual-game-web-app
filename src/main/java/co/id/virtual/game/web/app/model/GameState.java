package co.id.virtual.game.web.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Class representing the current state of a game.
 * This is used for real-time game state management and is not persisted directly.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameState {
    
    /**
     * The ID of the game.
     */
    private UUID gameId;
    
    /**
     * The ID of the game session.
     */
    private UUID sessionId;
    
    /**
     * The name of the game.
     */
    private String gameName;
    
    /**
     * The type of the game.
     */
    private GameType gameType;
    
    /**
     * The current state of the game (e.g., "waiting", "playing", "ended").
     */
    private String state;
    
    /**
     * The current round number.
     */
    private Integer roundNumber;
    
    /**
     * The ID of the user whose turn it is.
     */
    private UUID currentTurn;
    
    /**
     * The list of player IDs in the game.
     */
    @Builder.Default
    private List<UUID> players = new ArrayList<>();
    
    /**
     * Map of player IDs to their usernames.
     */
    @Builder.Default
    private Map<UUID, String> playerNames = new HashMap<>();
    
    /**
     * Map of player IDs to their current chip amounts in this session.
     */
    @Builder.Default
    private Map<UUID, Long> playerChips = new HashMap<>();
    
    /**
     * Map of player IDs to their current status in the game.
     */
    @Builder.Default
    private Map<UUID, String> playerStatus = new HashMap<>();
    
    /**
     * The timestamp of the last action.
     */
    private LocalDateTime lastActionTime;
    
    /**
     * The total pot size (for poker and similar games).
     */
    private Long potSize;
    
    /**
     * Game-specific data (depends on the game type).
     */
    @Builder.Default
    private Map<String, Object> gameData = new HashMap<>();
    
    /**
     * History of actions in the current round.
     */
    @Builder.Default
    private List<Map<String, Object>> actionHistory = new ArrayList<>();
    
    /**
     * Whether the game is active.
     */
    private boolean active;
    
    /**
     * The minimum bet amount.
     */
    private Integer minBet;
    
    /**
     * The maximum bet amount.
     */
    private Integer maxBet;
    
    /**
     * Add a player to the game.
     *
     * @param playerId the ID of the player
     * @param username the username of the player
     * @param chips the initial chip amount for the player
     */
    public void addPlayer(UUID playerId, String username, Long chips) {
        if (!players.contains(playerId)) {
            players.add(playerId);
            playerNames.put(playerId, username);
            playerChips.put(playerId, chips);
            playerStatus.put(playerId, "active");
        }
    }
    
    /**
     * Remove a player from the game.
     *
     * @param playerId the ID of the player
     */
    public void removePlayer(UUID playerId) {
        players.remove(playerId);
        playerNames.remove(playerId);
        playerChips.remove(playerId);
        playerStatus.remove(playerId);
    }
    
    /**
     * Update a player's chip amount.
     *
     * @param playerId the ID of the player
     * @param chips the new chip amount
     */
    public void updatePlayerChips(UUID playerId, Long chips) {
        playerChips.put(playerId, chips);
    }
    
    /**
     * Update a player's status.
     *
     * @param playerId the ID of the player
     * @param status the new status
     */
    public void updatePlayerStatus(UUID playerId, String status) {
        playerStatus.put(playerId, status);
    }
    
    /**
     * Add an action to the action history.
     *
     * @param action the action to add
     */
    public void addAction(Map<String, Object> action) {
        actionHistory.add(action);
        lastActionTime = LocalDateTime.now();
    }
    
    /**
     * Check if a player is in the game.
     *
     * @param playerId the ID of the player
     * @return true if the player is in the game, false otherwise
     */
    public boolean hasPlayer(UUID playerId) {
        return players.contains(playerId);
    }
    
    /**
     * Get the number of players in the game.
     *
     * @return the number of players
     */
    public int getPlayerCount() {
        return players.size();
    }
    
    /**
     * Check if the game is full.
     *
     * @param maxPlayers the maximum number of players
     * @return true if the game is full, false otherwise
     */
    public boolean isFull(int maxPlayers) {
        return players.size() >= maxPlayers;
    }
    
    /**
     * Check if the game has ended.
     *
     * @return true if the game has ended, false otherwise
     */
    public boolean hasEnded() {
        return "ended".equals(state);
    }
    
    /**
     * Check if it's a player's turn.
     *
     * @param playerId the ID of the player
     * @return true if it's the player's turn, false otherwise
     */
    public boolean isPlayerTurn(UUID playerId) {
        return playerId.equals(currentTurn);
    }
}
