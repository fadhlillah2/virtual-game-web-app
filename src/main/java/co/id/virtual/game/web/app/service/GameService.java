package co.id.virtual.game.web.app.service;

import co.id.virtual.game.web.app.model.Game;
import co.id.virtual.game.web.app.model.GameStatus;
import co.id.virtual.game.web.app.model.GameType;
import co.id.virtual.game.web.app.model.UserGameHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing games.
 */
public interface GameService {
    
    /**
     * Find a game by ID.
     *
     * @param id the ID of the game to find
     * @return an Optional containing the game if found, or empty if not found
     */
    Optional<Game> findById(UUID id);
    
    /**
     * Find a game by name.
     *
     * @param name the name of the game to find
     * @return an Optional containing the game if found, or empty if not found
     */
    Optional<Game> findByName(String name);
    
    /**
     * Find all active games.
     *
     * @return a list of all active games
     */
    List<Game> findAllActiveGames();
    
    /**
     * Find all games of a specific type.
     *
     * @param type the type of games to find
     * @return a list of all games of the specified type
     */
    List<Game> findByType(GameType type);
    
    /**
     * Find all active games of a specific type.
     *
     * @param type the type of games to find
     * @return a list of all active games of the specified type
     */
    List<Game> findActiveGamesByType(GameType type);
    
    /**
     * Find games with bet limits within a specified range.
     *
     * @param minBet the minimum bet amount
     * @param maxBet the maximum bet amount
     * @return a list of games with bet limits within the specified range
     */
    List<Game> findByBetRange(Integer minBet, Integer maxBet);
    
    /**
     * Find games that support a specific number of players.
     *
     * @param players the number of players
     * @return a list of games that support the specified number of players
     */
    List<Game> findByMaxPlayers(Integer players);
    
    /**
     * Create a new game.
     *
     * @param game the game to create
     * @return the created game
     */
    Game createGame(Game game);
    
    /**
     * Update an existing game.
     *
     * @param game the game to update
     * @return the updated game
     * @throws IllegalArgumentException if the game does not exist
     */
    Game updateGame(Game game);
    
    /**
     * Update a game's status.
     *
     * @param gameId the ID of the game to update
     * @param status the new status
     * @return the updated game
     * @throws IllegalArgumentException if the game does not exist
     */
    Game updateGameStatus(UUID gameId, GameStatus status);
    
    /**
     * Delete a game.
     *
     * @param gameId the ID of the game to delete
     * @throws IllegalArgumentException if the game does not exist
     */
    void deleteGame(UUID gameId);
    
    /**
     * Get a user's game history.
     *
     * @param userId the ID of the user
     * @param pageable pagination information
     * @return a page of the user's game history
     */
    Page<UserGameHistory> getUserGameHistory(UUID userId, Pageable pageable);
    
    /**
     * Get a user's game history for a specific game.
     *
     * @param userId the ID of the user
     * @param gameId the ID of the game
     * @param pageable pagination information
     * @return a page of the user's game history for the specified game
     */
    Page<UserGameHistory> getUserGameHistoryByGame(UUID userId, UUID gameId, Pageable pageable);
    
    /**
     * Count the number of games by type.
     *
     * @param type the game type
     * @return the number of games of the specified type
     */
    long countByType(GameType type);
}
