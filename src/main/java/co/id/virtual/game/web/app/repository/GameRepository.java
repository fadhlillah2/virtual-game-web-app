package co.id.virtual.game.web.app.repository;

import co.id.virtual.game.web.app.model.Game;
import co.id.virtual.game.web.app.model.GameStatus;
import co.id.virtual.game.web.app.model.GameType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Game entity.
 * Provides methods to access and manipulate game data.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {
    
    /**
     * Find all active games.
     *
     * @return a list of active games
     */
    List<Game> findByStatus(GameStatus status);
    
    /**
     * Find all games of a specific type.
     *
     * @param type the game type to search for
     * @return a list of games of the specified type
     */
    List<Game> findByType(GameType type);
    
    /**
     * Find all active games of a specific type.
     *
     * @param type the game type to search for
     * @param status the game status to search for
     * @return a list of active games of the specified type
     */
    List<Game> findByTypeAndStatus(GameType type, GameStatus status);
    
    /**
     * Find a game by name.
     *
     * @param name the name to search for
     * @return an Optional containing the game if found, or empty if not found
     */
    Optional<Game> findByName(String name);
    
    /**
     * Find games with bet limits within a specified range.
     *
     * @param minBet the minimum bet amount
     * @param maxBet the maximum bet amount
     * @return a list of games with bet limits within the specified range
     */
    @Query("SELECT g FROM Game g WHERE g.minBet >= :minBet AND g.maxBet <= :maxBet")
    List<Game> findByBetRange(@Param("minBet") Integer minBet, @Param("maxBet") Integer maxBet);
    
    /**
     * Find games that support a specific number of players.
     *
     * @param players the number of players
     * @return a list of games that support the specified number of players
     */
    List<Game> findByMaxPlayersGreaterThanEqual(Integer players);
    
    /**
     * Count the number of games by type.
     *
     * @param type the game type
     * @return the number of games of the specified type
     */
    long countByType(GameType type);
}
