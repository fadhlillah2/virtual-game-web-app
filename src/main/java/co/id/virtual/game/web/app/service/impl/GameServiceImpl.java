package co.id.virtual.game.web.app.service.impl;

import co.id.virtual.game.web.app.model.Game;
import co.id.virtual.game.web.app.model.GameStatus;
import co.id.virtual.game.web.app.model.GameType;
import co.id.virtual.game.web.app.model.UserGameHistory;
import co.id.virtual.game.web.app.repository.GameRepository;
import co.id.virtual.game.web.app.repository.UserGameHistoryRepository;
import co.id.virtual.game.web.app.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the GameService interface.
 */
@Service
public class GameServiceImpl implements GameService {
    
    private final GameRepository gameRepository;
    private final UserGameHistoryRepository userGameHistoryRepository;
    
    @Autowired
    public GameServiceImpl(GameRepository gameRepository, UserGameHistoryRepository userGameHistoryRepository) {
        this.gameRepository = gameRepository;
        this.userGameHistoryRepository = userGameHistoryRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Game> findById(UUID id) {
        return gameRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Game> findByName(String name) {
        return gameRepository.findByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Game> findAllActiveGames() {
        return gameRepository.findByStatus(GameStatus.ACTIVE);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Game> findByType(GameType type) {
        return gameRepository.findByType(type);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Game> findActiveGamesByType(GameType type) {
        return gameRepository.findByTypeAndStatus(type, GameStatus.ACTIVE);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Game> findByBetRange(Integer minBet, Integer maxBet) {
        return gameRepository.findByBetRange(minBet, maxBet);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Game> findByMaxPlayers(Integer players) {
        return gameRepository.findByMaxPlayersGreaterThanEqual(players);
    }
    
    @Override
    @Transactional
    public Game createGame(Game game) {
        // Set default values if not provided
        if (game.getStatus() == null) {
            game.setStatus(GameStatus.ACTIVE);
        }
        if (game.getMinBet() == null) {
            game.setMinBet(10);
        }
        if (game.getMaxBet() == null) {
            game.setMaxBet(1000);
        }
        if (game.getMaxPlayers() == null) {
            game.setMaxPlayers(6);
        }
        
        return gameRepository.save(game);
    }
    
    @Override
    @Transactional
    public Game updateGame(Game game) {
        if (!gameRepository.existsById(game.getId())) {
            throw new IllegalArgumentException("Game not found with ID: " + game.getId());
        }
        
        return gameRepository.save(game);
    }
    
    @Override
    @Transactional
    public Game updateGameStatus(UUID gameId, GameStatus status) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found with ID: " + gameId));
        
        game.setStatus(status);
        return gameRepository.save(game);
    }
    
    @Override
    @Transactional
    public void deleteGame(UUID gameId) {
        if (!gameRepository.existsById(gameId)) {
            throw new IllegalArgumentException("Game not found with ID: " + gameId);
        }
        
        gameRepository.deleteById(gameId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<UserGameHistory> getUserGameHistory(UUID userId, Pageable pageable) {
        return userGameHistoryRepository.findByUserId(userId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<UserGameHistory> getUserGameHistoryByGame(UUID userId, UUID gameId, Pageable pageable) {
        return userGameHistoryRepository.findByUserIdAndGameId(userId, gameId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByType(GameType type) {
        return gameRepository.countByType(type);
    }
}
