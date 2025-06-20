package co.id.virtual.game.web.app.config;

import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for rate limiting.
 * This class sets up rate limiters for authentication endpoints and sensitive operations
 * to prevent brute force attacks and abuse.
 */
@Configuration
public class RateLimitingConfig {

    private final RedissonClient redissonClient;

    @Autowired
    public RateLimitingConfig(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * Creates a rate limiter for the login endpoint.
     * Limits to 5 requests per minute per IP address.
     *
     * @return the configured rate limiter
     */
    @Bean
    public RRateLimiter loginRateLimiter() {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter("login-rate-limiter");
        // Initialize rate limiter with 5 requests per minute
        rateLimiter.trySetRate(RateType.OVERALL, 5, 1, RateIntervalUnit.MINUTES);
        return rateLimiter;
    }

    /**
     * Creates a rate limiter for the register endpoint.
     * Limits to 3 requests per minute per IP address.
     *
     * @return the configured rate limiter
     */
    @Bean
    public RRateLimiter registerRateLimiter() {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter("register-rate-limiter");
        // Initialize rate limiter with 3 requests per minute
        rateLimiter.trySetRate(RateType.OVERALL, 3, 1, RateIntervalUnit.MINUTES);
        return rateLimiter;
    }

    /**
     * Creates a rate limiter for the refresh token endpoint.
     * Limits to 10 requests per minute per IP address.
     *
     * @return the configured rate limiter
     */
    @Bean
    public RRateLimiter refreshTokenRateLimiter() {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter("refresh-token-rate-limiter");
        // Initialize rate limiter with 10 requests per minute
        rateLimiter.trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.MINUTES);
        return rateLimiter;
    }

    /**
     * Creates a rate limiter for the daily bonus endpoint.
     * Limits to 2 requests per hour per IP address to prevent abuse.
     *
     * @return the configured rate limiter
     */
    @Bean
    public RRateLimiter dailyBonusRateLimiter() {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter("daily-bonus-rate-limiter");
        // Initialize rate limiter with 2 requests per hour
        rateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.HOURS);
        return rateLimiter;
    }

    /**
     * Creates a rate limiter for the gift sending endpoint.
     * Limits to 5 requests per hour per IP address to prevent abuse.
     *
     * @return the configured rate limiter
     */
    @Bean
    public RRateLimiter giftRateLimiter() {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter("gift-rate-limiter");
        // Initialize rate limiter with 5 requests per hour
        rateLimiter.trySetRate(RateType.OVERALL, 5, 1, RateIntervalUnit.HOURS);
        return rateLimiter;
    }

    /**
     * Creates a rate limiter for the user profile update endpoint.
     * Limits to 10 requests per hour per IP address to prevent abuse.
     *
     * @return the configured rate limiter
     */
    @Bean
    public RRateLimiter profileUpdateRateLimiter() {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter("profile-update-rate-limiter");
        // Initialize rate limiter with 10 requests per hour
        rateLimiter.trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.HOURS);
        return rateLimiter;
    }

    /**
     * Creates a rate limiter for the game session join endpoint.
     * Limits to 20 requests per hour per IP address to prevent abuse.
     *
     * @return the configured rate limiter
     */
    @Bean
    public RRateLimiter gameSessionJoinRateLimiter() {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter("game-session-join-rate-limiter");
        // Initialize rate limiter with 20 requests per hour
        rateLimiter.trySetRate(RateType.OVERALL, 20, 1, RateIntervalUnit.HOURS);
        return rateLimiter;
    }
}
