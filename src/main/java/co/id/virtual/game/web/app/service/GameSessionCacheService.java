package co.id.virtual.game.web.app.service;

import co.id.virtual.game.web.app.model.SessionData;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

/**
 * Service for caching game sessions in Redis.
 */
@Service
@Slf4j
public class GameSessionCacheService {
    
    private final RedissonClient redissonClient;
    
    private static final String GAME_SESSION_PREFIX = "game:session:";
    private static final String PLAYER_SESSION_PREFIX = "player:session:";
    private static final Duration SESSION_CACHE_TTL = Duration.ofHours(2);
    
    @Autowired
    public GameSessionCacheService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
    
    /**
     * Cache a game session.
     *
     * @param sessionId the ID of the session
     * @param sessionData the session data
     */
    public void cacheGameSession(UUID sessionId, SessionData sessionData) {
        RBucket<SessionData> bucket = redissonClient.getBucket(
            GAME_SESSION_PREFIX + sessionId.toString()
        );
        bucket.set(sessionData, SESSION_CACHE_TTL);
        
        log.debug("Cached game session: {}", sessionId);
    }
    
    /**
     * Get a game session from cache.
     *
     * @param sessionId the ID of the session
     * @return the session data, or null if not found
     */
    public SessionData getGameSession(UUID sessionId) {
        RBucket<SessionData> bucket = redissonClient.getBucket(
            GAME_SESSION_PREFIX + sessionId.toString()
        );
        return bucket.get();
    }
    
    /**
     * Remove a game session from cache.
     *
     * @param sessionId the ID of the session
     */
    public void removeGameSession(UUID sessionId) {
        redissonClient.getBucket(GAME_SESSION_PREFIX + sessionId.toString()).delete();
        log.debug("Removed game session from cache: {}", sessionId);
    }
    
    /**
     * Add a player to a session.
     *
     * @param userId the ID of the player
     * @param sessionId the ID of the session
     */
    public void addPlayerToSession(UUID userId, UUID sessionId) {
        RSet<UUID> playerSessions = redissonClient.getSet(
            PLAYER_SESSION_PREFIX + userId.toString()
        );
        playerSessions.add(sessionId);
        playerSessions.expire(SESSION_CACHE_TTL);
    }
    
    /**
     * Get all sessions for a player.
     *
     * @param userId the ID of the player
     * @return the set of session IDs
     */
    public Set<UUID> getPlayerSessions(UUID userId) {
        RSet<UUID> playerSessions = redissonClient.getSet(
            PLAYER_SESSION_PREFIX + userId.toString()
        );
        return playerSessions.readAll();
    }
    
    /**
     * Remove a player from a session.
     *
     * @param userId the ID of the player
     * @param sessionId the ID of the session
     */
    public void removePlayerFromSession(UUID userId, UUID sessionId) {
        RSet<UUID> playerSessions = redissonClient.getSet(
            PLAYER_SESSION_PREFIX + userId.toString()
        );
        playerSessions.remove(sessionId);
    }
    
    /**
     * Check if a session exists in cache.
     *
     * @param sessionId the ID of the session
     * @return true if the session exists, false otherwise
     */
    public boolean sessionExists(UUID sessionId) {
        return redissonClient.getBucket(GAME_SESSION_PREFIX + sessionId.toString()).isExists();
    }
    
    /**
     * Update the TTL for a session.
     *
     * @param sessionId the ID of the session
     */
    public void touchSession(UUID sessionId) {
        RBucket<SessionData> bucket = redissonClient.getBucket(
            GAME_SESSION_PREFIX + sessionId.toString()
        );
        if (bucket.isExists()) {
            bucket.expire(SESSION_CACHE_TTL);
        }
    }
}
