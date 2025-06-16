package co.id.virtual.game.web.app.service.impl;

import co.id.virtual.game.web.app.dto.leaderboard.LeaderboardEntryDto;
import co.id.virtual.game.web.app.model.GameType;
import co.id.virtual.game.web.app.model.User;
import co.id.virtual.game.web.app.repository.UserGameHistoryRepository;
import co.id.virtual.game.web.app.repository.UserRepository;
import co.id.virtual.game.web.app.service.LeaderboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the LeaderboardService interface.
 */
@Service
@Slf4j
public class LeaderboardServiceImpl implements LeaderboardService {
    
    private final UserRepository userRepository;
    private final UserGameHistoryRepository userGameHistoryRepository;
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    public LeaderboardServiceImpl(
            UserRepository userRepository,
            UserGameHistoryRepository userGameHistoryRepository,
            JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.userGameHistoryRepository = userGameHistoryRepository;
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
     * Get the global leaderboard.
     *
     * @param type the type of leaderboard (chips, wins, etc.)
     * @param limit the maximum number of entries to return
     * @return the leaderboard entries
     */
    @Override
    @Cacheable(value = "leaderboards", key = "'global_' + #type + '_' + #limit")
    public List<LeaderboardEntryDto> getLeaderboard(String type, int limit) {
        String orderBy;
        
        switch (type.toLowerCase()) {
            case "chips":
                orderBy = "chips_balance DESC";
                break;
            case "wins":
                orderBy = "games_won DESC";
                break;
            case "winrate":
                orderBy = "CASE WHEN games_played > 0 THEN CAST(games_won AS FLOAT) / games_played ELSE 0 END DESC";
                break;
            default:
                orderBy = "chips_balance DESC";
        }
        
        String sql = "SELECT id, username, avatar_url, chips_balance, games_played, games_won, " +
                "CASE WHEN games_played > 0 THEN CAST(games_won AS FLOAT) / games_played ELSE 0 END AS win_rate, " +
                "RANK() OVER (ORDER BY " + orderBy + ") as rank " +
                "FROM users " +
                "ORDER BY " + orderBy + " " +
                "LIMIT ?";
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            LeaderboardEntryDto entry = new LeaderboardEntryDto();
            entry.setId(rs.getString("id"));
            entry.setUsername(rs.getString("username"));
            entry.setAvatarUrl(rs.getString("avatar_url"));
            entry.setRank(rs.getInt("rank"));
            entry.setScore(rs.getLong("chips_balance"));
            entry.setGamesPlayed(rs.getInt("games_played"));
            entry.setGamesWon(rs.getInt("games_won"));
            entry.setWinRate(rs.getDouble("win_rate"));
            return entry;
        }, limit);
    }
    
    /**
     * Get the game-specific leaderboard.
     *
     * @param gameType the type of game
     * @param limit the maximum number of entries to return
     * @return the game-specific leaderboard entries
     */
    @Override
    @Cacheable(value = "leaderboards", key = "'game_' + #gameType + '_' + #limit")
    public List<LeaderboardEntryDto> getGameLeaderboard(String gameType, int limit) {
        String sql = "SELECT u.id, u.username, u.avatar_url, " +
                "COUNT(h.id) as games_played, " +
                "SUM(CASE WHEN h.result = 'WIN' THEN 1 ELSE 0 END) as games_won, " +
                "SUM(h.chips_change) as total_chips, " +
                "CASE WHEN COUNT(h.id) > 0 THEN CAST(SUM(CASE WHEN h.result = 'WIN' THEN 1 ELSE 0 END) AS FLOAT) / COUNT(h.id) ELSE 0 END AS win_rate, " +
                "RANK() OVER (ORDER BY SUM(h.chips_change) DESC) as rank " +
                "FROM users u " +
                "JOIN user_game_history h ON u.id = h.user_id " +
                "JOIN games g ON h.game_id = g.id " +
                "WHERE g.type = ? " +
                "GROUP BY u.id, u.username, u.avatar_url " +
                "ORDER BY total_chips DESC " +
                "LIMIT ?";
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            LeaderboardEntryDto entry = new LeaderboardEntryDto();
            entry.setId(rs.getString("id"));
            entry.setUsername(rs.getString("username"));
            entry.setAvatarUrl(rs.getString("avatar_url"));
            entry.setRank(rs.getInt("rank"));
            entry.setScore(rs.getLong("total_chips"));
            entry.setGamesPlayed(rs.getInt("games_played"));
            entry.setGamesWon(rs.getInt("games_won"));
            entry.setWinRate(rs.getDouble("win_rate"));
            return entry;
        }, gameType, limit);
    }
    
    /**
     * Get the user's rank on the global leaderboard.
     *
     * @param userId the user ID
     * @param type the type of leaderboard (chips, wins, etc.)
     * @return the user's leaderboard entry
     */
    @Override
    public LeaderboardEntryDto getUserRank(String userId, String type) {
        Optional<User> userOpt = userRepository.findById(UUID.fromString(userId));
        
        if (userOpt.isEmpty()) {
            return null;
        }
        
        User user = userOpt.get();
        String orderBy;
        
        switch (type.toLowerCase()) {
            case "chips":
                orderBy = "chips_balance DESC";
                break;
            case "wins":
                orderBy = "games_won DESC";
                break;
            case "winrate":
                orderBy = "CASE WHEN games_played > 0 THEN CAST(games_won AS FLOAT) / games_played ELSE 0 END DESC";
                break;
            default:
                orderBy = "chips_balance DESC";
        }
        
        String sql = "SELECT id, username, avatar_url, chips_balance, games_played, games_won, " +
                "CASE WHEN games_played > 0 THEN CAST(games_won AS FLOAT) / games_played ELSE 0 END AS win_rate, " +
                "RANK() OVER (ORDER BY " + orderBy + ") as rank " +
                "FROM users " +
                "WHERE id = ?";
        
        List<LeaderboardEntryDto> results = jdbcTemplate.query(sql, (rs, rowNum) -> {
            LeaderboardEntryDto entry = new LeaderboardEntryDto();
            entry.setId(rs.getString("id"));
            entry.setUsername(rs.getString("username"));
            entry.setAvatarUrl(rs.getString("avatar_url"));
            entry.setRank(rs.getInt("rank"));
            entry.setScore(rs.getLong("chips_balance"));
            entry.setGamesPlayed(rs.getInt("games_played"));
            entry.setGamesWon(rs.getInt("games_won"));
            entry.setWinRate(rs.getDouble("win_rate"));
            return entry;
        }, userId);
        
        return results.isEmpty() ? null : results.get(0);
    }
    
    /**
     * Get the user's rank on the game-specific leaderboard.
     *
     * @param userId the user ID
     * @param gameType the type of game
     * @return the user's game-specific leaderboard entry
     */
    @Override
    public LeaderboardEntryDto getUserGameRank(String userId, String gameType) {
        String sql = "SELECT u.id, u.username, u.avatar_url, " +
                "COUNT(h.id) as games_played, " +
                "SUM(CASE WHEN h.result = 'WIN' THEN 1 ELSE 0 END) as games_won, " +
                "SUM(h.chips_change) as total_chips, " +
                "CASE WHEN COUNT(h.id) > 0 THEN CAST(SUM(CASE WHEN h.result = 'WIN' THEN 1 ELSE 0 END) AS FLOAT) / COUNT(h.id) ELSE 0 END AS win_rate, " +
                "RANK() OVER (ORDER BY SUM(h.chips_change) DESC) as rank " +
                "FROM users u " +
                "JOIN user_game_history h ON u.id = h.user_id " +
                "JOIN games g ON h.game_id = g.id " +
                "WHERE g.type = ? AND u.id = ? " +
                "GROUP BY u.id, u.username, u.avatar_url";
        
        List<LeaderboardEntryDto> results = jdbcTemplate.query(sql, (rs, rowNum) -> {
            LeaderboardEntryDto entry = new LeaderboardEntryDto();
            entry.setId(rs.getString("id"));
            entry.setUsername(rs.getString("username"));
            entry.setAvatarUrl(rs.getString("avatar_url"));
            entry.setRank(rs.getInt("rank"));
            entry.setScore(rs.getLong("total_chips"));
            entry.setGamesPlayed(rs.getInt("games_played"));
            entry.setGamesWon(rs.getInt("games_won"));
            entry.setWinRate(rs.getDouble("win_rate"));
            return entry;
        }, gameType, userId);
        
        return results.isEmpty() ? null : results.get(0);
    }
}
