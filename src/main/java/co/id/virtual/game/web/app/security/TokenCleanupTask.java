package co.id.virtual.game.web.app.security;

import co.id.virtual.game.web.app.repository.BlacklistedTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Scheduled task to clean up expired tokens from the blacklist.
 * This is a safety measure to ensure that any tokens that might have been added
 * without a TTL are eventually removed.
 */
@Component
public class TokenCleanupTask {

    private static final Logger logger = LoggerFactory.getLogger(TokenCleanupTask.class);
    private static final String BLACKLIST_KEY_PREFIX = "blacklisted_token:";
    
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;
    
    @Autowired
    public TokenCleanupTask(RedisTemplate<String, String> redisTemplate, JwtUtil jwtUtil) {
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
    }
    
    /**
     * Scheduled task that runs daily at 2 AM to clean up expired tokens from the blacklist.
     * This is a safety measure to ensure that any tokens that might have been added
     * without a TTL are eventually removed.
     */
    @Scheduled(cron = "0 0 2 * * ?") // Run at 2 AM every day
    public void cleanupExpiredTokens() {
        logger.info("Starting scheduled cleanup of expired blacklisted tokens");
        
        try {
            // Get all keys with the blacklist prefix
            Set<String> keys = redisTemplate.keys(BLACKLIST_KEY_PREFIX + "*");
            
            if (keys == null || keys.isEmpty()) {
                logger.info("No blacklisted tokens found to clean up");
                return;
            }
            
            int expiredCount = 0;
            int errorCount = 0;
            
            for (String key : keys) {
                try {
                    // Extract the token from the key
                    String token = key.substring(BLACKLIST_KEY_PREFIX.length());
                    
                    // Check if the token is expired
                    boolean isExpired = jwtUtil.isTokenExpired(token);
                    
                    if (isExpired) {
                        // Delete the key
                        Boolean deleted = redisTemplate.delete(key);
                        if (Boolean.TRUE.equals(deleted)) {
                            expiredCount++;
                            logger.debug("Removed expired token from blacklist: {}", key);
                        }
                    }
                } catch (Exception e) {
                    // Log the error but continue processing other tokens
                    errorCount++;
                    logger.warn("Error processing token key {}: {}", key, e.getMessage());
                }
            }
            
            logger.info("Completed blacklisted token cleanup: removed {} expired tokens, encountered {} errors", 
                    expiredCount, errorCount);
            
        } catch (RedisConnectionFailureException e) {
            logger.error("Redis connection failure during blacklisted token cleanup: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error during blacklisted token cleanup: {}", e.getMessage());
        }
    }
}
