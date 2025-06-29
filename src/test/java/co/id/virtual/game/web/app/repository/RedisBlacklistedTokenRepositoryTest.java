package co.id.virtual.game.web.app.repository;

import co.id.virtual.game.web.app.repository.impl.RedisBlacklistedTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RedisBlacklistedTokenRepositoryTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private BlacklistedTokenRepository blacklistedTokenRepository;

    private static final String TEST_TOKEN = "test.jwt.token";
    private static final String BLACKLIST_KEY = "blacklisted_token:test.jwt.token";

    @BeforeEach
    void setUp() {
        blacklistedTokenRepository = new RedisBlacklistedTokenRepository(redisTemplate);
    }

    @Test
    void blacklistToken_ShouldStoreTokenInRedis() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        long expirationTime = System.currentTimeMillis() + 3600000; // 1 hour from now

        // Act
        blacklistedTokenRepository.blacklistToken(TEST_TOKEN, expirationTime);

        // Assert
        verify(valueOperations).set(eq(BLACKLIST_KEY), eq("blacklisted"));
        verify(redisTemplate).expire(eq(BLACKLIST_KEY), anyLong(), eq(TimeUnit.MILLISECONDS));
        
        System.out.println("[DEBUG_LOG] Token blacklisted: " + TEST_TOKEN);
    }

    @Test
    void isTokenBlacklisted_ShouldReturnTrue_WhenTokenExists() {
        // Arrange
        when(redisTemplate.hasKey(BLACKLIST_KEY)).thenReturn(true);

        // Act
        boolean result = blacklistedTokenRepository.isTokenBlacklisted(TEST_TOKEN);

        // Assert
        assertTrue(result);
        verify(redisTemplate).hasKey(BLACKLIST_KEY);
        
        System.out.println("[DEBUG_LOG] Token is blacklisted: " + result);
    }

    @Test
    void isTokenBlacklisted_ShouldReturnFalse_WhenTokenDoesNotExist() {
        // Arrange
        when(redisTemplate.hasKey(BLACKLIST_KEY)).thenReturn(false);

        // Act
        boolean result = blacklistedTokenRepository.isTokenBlacklisted(TEST_TOKEN);

        // Assert
        assertFalse(result);
        verify(redisTemplate).hasKey(BLACKLIST_KEY);
        
        System.out.println("[DEBUG_LOG] Token is blacklisted: " + result);
    }

    @Test
    void removeFromBlacklist_ShouldDeleteTokenFromRedis() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Act
        blacklistedTokenRepository.removeFromBlacklist(TEST_TOKEN);

        // Assert
        verify(redisTemplate).delete(BLACKLIST_KEY);
        
        System.out.println("[DEBUG_LOG] Token removed from blacklist: " + TEST_TOKEN);
    }
}
