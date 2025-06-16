package co.id.virtual.game.web.app.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for Redis caching with Redisson.
 */
@Configuration
@Slf4j
public class RedissonConfig {
    
    @Value("${spring.data.redis.host}")
    private String redisHost;
    
    @Value("${spring.data.redis.port}")
    private int redisPort;
    
    @Value("${spring.data.redis.password:}")
    private String redisPassword;
    
    /**
     * Configure the Redisson client.
     *
     * @return the Redisson client
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        
        String address = "redis://" + redisHost + ":" + redisPort;
        log.info("Configuring Redisson with Redis address: {}", address);
        
        if (redisPassword != null && !redisPassword.isEmpty()) {
            config.useSingleServer()
                  .setAddress(address)
                  .setPassword(redisPassword)
                  .setConnectionMinimumIdleSize(5)
                  .setConnectionPoolSize(10)
                  .setIdleConnectionTimeout(10000)
                  .setConnectTimeout(10000)
                  .setTimeout(3000)
                  .setRetryAttempts(3)
                  .setRetryInterval(1500);
        } else {
            config.useSingleServer()
                  .setAddress(address)
                  .setConnectionMinimumIdleSize(5)
                  .setConnectionPoolSize(10)
                  .setIdleConnectionTimeout(10000)
                  .setConnectTimeout(10000)
                  .setTimeout(3000)
                  .setRetryAttempts(3)
                  .setRetryInterval(1500);
        }
        
        return Redisson.create(config);
    }
    
    /**
     * Configure the cache manager.
     *
     * @param redissonClient the Redisson client
     * @return the cache manager
     */
    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> configMap = new HashMap<>();
        
        // User cache - 30 minutes TTL
        configMap.put("users", new CacheConfig(
            Duration.ofMinutes(30).toMillis(),
            Duration.ofMinutes(10).toMillis()
        ));
        
        // Game state cache - 5 minutes TTL
        configMap.put("gameStates", new CacheConfig(
            Duration.ofMinutes(5).toMillis(),
            Duration.ofMinutes(2).toMillis()
        ));
        
        // Leaderboard cache - 1 hour TTL
        configMap.put("leaderboards", new CacheConfig(
            Duration.ofHours(1).toMillis(),
            Duration.ofMinutes(30).toMillis()
        ));
        
        // Transaction cache - 15 minutes TTL
        configMap.put("transactions", new CacheConfig(
            Duration.ofMinutes(15).toMillis(),
            Duration.ofMinutes(5).toMillis()
        ));
        
        return new RedissonSpringCacheManager(redissonClient, configMap);
    }
}
