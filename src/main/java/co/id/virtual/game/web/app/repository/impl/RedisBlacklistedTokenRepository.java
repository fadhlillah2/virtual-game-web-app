package co.id.virtual.game.web.app.repository.impl;

import co.id.virtual.game.web.app.repository.BlacklistedTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Redis implementation of the BlacklistedTokenRepository interface.
 */
@Repository
public class RedisBlacklistedTokenRepository implements BlacklistedTokenRepository {

    private static final Logger logger = LoggerFactory.getLogger(RedisBlacklistedTokenRepository.class);
    private static final String BLACKLIST_KEY_PREFIX = "blacklisted_token:";
    private static final String USER_TOKEN_KEY_PREFIX = "user_tokens:";

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisBlacklistedTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void blacklistToken(String token, long expirationTime) {
        blacklistToken(token, expirationTime, null);
    }

    @Override
    public void blacklistToken(String token, long expirationTime, UUID userId) {
        if (token == null || token.isEmpty()) {
            logger.warn("Attempted to blacklist null or empty token");
            return;
        }

        String key = BLACKLIST_KEY_PREFIX + token;
        try {
            // Store token in blacklist
            String value = userId != null ? userId.toString() : "blacklisted";
            redisTemplate.opsForValue().set(key, value);

            // Calculate time to live in milliseconds
            long ttl = expirationTime - System.currentTimeMillis();
            if (ttl > 0) {
                redisTemplate.expire(key, ttl, TimeUnit.MILLISECONDS);
                logger.debug("Token blacklisted with TTL of {} ms", ttl);
            } else {
                // If token is already expired, set a short TTL to avoid storing it forever
                redisTemplate.expire(key, 60, TimeUnit.SECONDS);
                logger.debug("Token already expired, blacklisted with 60s TTL");
            }

            // If userId is provided, add token to user's token set
            if (userId != null) {
                String userTokensKey = USER_TOKEN_KEY_PREFIX + userId.toString();
                redisTemplate.opsForSet().add(userTokensKey, token);

                // Set the same expiration for the user tokens key
                if (ttl > 0) {
                    // We use the maximum TTL for the user tokens key to ensure it exists as long as any token exists
                    Long currentTtl = redisTemplate.getExpire(userTokensKey, TimeUnit.MILLISECONDS);
                    if (currentTtl == null || currentTtl < 0 || ttl > currentTtl) {
                        redisTemplate.expire(userTokensKey, ttl, TimeUnit.MILLISECONDS);
                    }
                }
            }

            logger.info("Successfully blacklisted token" + (userId != null ? " for user " + userId : ""));
        } catch (RedisConnectionFailureException e) {
            logger.error("Redis connection failure while blacklisting token: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error blacklisting token: {}", e.getMessage());
            throw new RuntimeException("Failed to blacklist token", e);
        }
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        if (token == null || token.isEmpty()) {
            logger.warn("Attempted to check blacklist status for null or empty token");
            return false;
        }

        String key = BLACKLIST_KEY_PREFIX + token;
        try {
            Boolean isBlacklisted = redisTemplate.hasKey(key);
            if (Boolean.TRUE.equals(isBlacklisted)) {
                logger.debug("Token is blacklisted: {}", token);
            }
            return Boolean.TRUE.equals(isBlacklisted);
        } catch (RedisConnectionFailureException e) {
            logger.error("Redis connection failure while checking token blacklist status: {}", e.getMessage());
            // Default to not blacklisted on connection error to avoid blocking legitimate users
            // This is a trade-off between security and availability
            return false;
        } catch (Exception e) {
            logger.error("Error checking if token is blacklisted: {}", e.getMessage());
            // Default to not blacklisted on error
            return false;
        }
    }

    @Override
    public void removeFromBlacklist(String token) {
        if (token == null || token.isEmpty()) {
            logger.warn("Attempted to remove null or empty token from blacklist");
            return;
        }

        String key = BLACKLIST_KEY_PREFIX + token;
        try {
            // Get the user ID associated with the token before deleting
            String userId = redisTemplate.opsForValue().get(key);

            // Delete the token from the blacklist
            Boolean deleted = redisTemplate.delete(key);

            // If the token was associated with a user, remove it from the user's token set
            if (userId != null && !userId.equals("blacklisted")) {
                String userTokensKey = USER_TOKEN_KEY_PREFIX + userId;
                redisTemplate.opsForSet().remove(userTokensKey, token);
            }

            if (Boolean.TRUE.equals(deleted)) {
                logger.info("Token removed from blacklist: {}", token);
            } else {
                logger.debug("Token was not in blacklist: {}", token);
            }
        } catch (RedisConnectionFailureException e) {
            logger.error("Redis connection failure while removing token from blacklist: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error removing token from blacklist: {}", e.getMessage());
            throw new RuntimeException("Failed to remove token from blacklist", e);
        }
    }

    @Override
    public int blacklistAllUserTokens(UUID userId) {
        if (userId == null) {
            logger.warn("Attempted to blacklist tokens for null user ID");
            return 0;
        }

        String userTokensKey = USER_TOKEN_KEY_PREFIX + userId.toString();
        try {
            // Get all tokens for the user
            Set<String> tokens = redisTemplate.opsForSet().members(userTokensKey);
            if (tokens == null || tokens.isEmpty()) {
                logger.info("No tokens found for user {}", userId);
                return 0;
            }

            int count = 0;
            for (String token : tokens) {
                // Check if the token is already blacklisted
                String key = BLACKLIST_KEY_PREFIX + token;
                Boolean exists = redisTemplate.hasKey(key);

                if (Boolean.TRUE.equals(exists)) {
                    // Token is already blacklisted, update the expiration time
                    // We set a long expiration time to ensure the token remains blacklisted
                    redisTemplate.expire(key, 30, TimeUnit.DAYS);
                    count++;
                }
            }

            logger.info("Blacklisted {} tokens for user {}", count, userId);
            return count;
        } catch (RedisConnectionFailureException e) {
            logger.error("Redis connection failure while blacklisting user tokens: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error blacklisting user tokens: {}", e.getMessage());
            throw new RuntimeException("Failed to blacklist user tokens", e);
        }
    }

    @Override
    public List<String> getBlacklistedTokensForUser(UUID userId) {
        if (userId == null) {
            logger.warn("Attempted to get tokens for null user ID");
            return new ArrayList<>();
        }

        String userTokensKey = USER_TOKEN_KEY_PREFIX + userId.toString();
        try {
            // Get all tokens for the user
            Set<String> tokens = redisTemplate.opsForSet().members(userTokensKey);
            if (tokens == null) {
                return new ArrayList<>();
            }

            List<String> blacklistedTokens = new ArrayList<>();
            for (String token : tokens) {
                // Check if the token is still blacklisted
                String key = BLACKLIST_KEY_PREFIX + token;
                Boolean exists = redisTemplate.hasKey(key);

                if (Boolean.TRUE.equals(exists)) {
                    blacklistedTokens.add(token);
                } else {
                    // Token is no longer blacklisted, remove it from the user's token set
                    redisTemplate.opsForSet().remove(userTokensKey, token);
                }
            }

            return blacklistedTokens;
        } catch (RedisConnectionFailureException e) {
            logger.error("Redis connection failure while getting user tokens: {}", e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error getting user tokens: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
}
