package co.id.virtual.game.web.app.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the TokenCleanupTask.
 */
@ExtendWith(MockitoExtension.class)
public class TokenCleanupTaskTest {

    private static final String BLACKLIST_KEY_PREFIX = "blacklisted_token:";

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private TokenCleanupTask tokenCleanupTask;

    private Set<String> mockKeys;
    private String expiredToken;
    private String validToken;

    @BeforeEach
    void setUp() {
        // Create mock keys
        mockKeys = new HashSet<>();
        expiredToken = "expired.test.token";
        validToken = "valid.test.token";
        
        mockKeys.add(BLACKLIST_KEY_PREFIX + expiredToken);
        mockKeys.add(BLACKLIST_KEY_PREFIX + validToken);
        
        // Mock JwtUtil behavior
        when(jwtUtil.isTokenExpired(expiredToken)).thenReturn(true);
        when(jwtUtil.isTokenExpired(validToken)).thenReturn(false);
    }

    @Test
    void cleanupExpiredTokens_ShouldRemoveExpiredTokens() {
        // Arrange
        when(redisTemplate.keys(BLACKLIST_KEY_PREFIX + "*")).thenReturn(mockKeys);
        when(redisTemplate.delete(BLACKLIST_KEY_PREFIX + expiredToken)).thenReturn(true);
        
        System.out.println("[DEBUG_LOG] Testing cleanupExpiredTokens with mock keys: " + mockKeys);

        // Act
        tokenCleanupTask.cleanupExpiredTokens();

        // Assert
        verify(redisTemplate).keys(BLACKLIST_KEY_PREFIX + "*");
        verify(jwtUtil).isTokenExpired(expiredToken);
        verify(jwtUtil).isTokenExpired(validToken);
        verify(redisTemplate).delete(BLACKLIST_KEY_PREFIX + expiredToken);
        verify(redisTemplate, never()).delete(BLACKLIST_KEY_PREFIX + validToken);
        
        System.out.println("[DEBUG_LOG] Verified that only expired token was deleted");
    }

    @Test
    void cleanupExpiredTokens_ShouldHandleEmptyKeySet() {
        // Arrange
        when(redisTemplate.keys(BLACKLIST_KEY_PREFIX + "*")).thenReturn(new HashSet<>());
        
        System.out.println("[DEBUG_LOG] Testing cleanupExpiredTokens with empty key set");

        // Act
        tokenCleanupTask.cleanupExpiredTokens();

        // Assert
        verify(redisTemplate).keys(BLACKLIST_KEY_PREFIX + "*");
        verify(jwtUtil, never()).isTokenExpired(anyString());
        verify(redisTemplate, never()).delete(anyString());
        
        System.out.println("[DEBUG_LOG] Verified that no tokens were processed when key set is empty");
    }

    @Test
    void cleanupExpiredTokens_ShouldHandleNullKeySet() {
        // Arrange
        when(redisTemplate.keys(BLACKLIST_KEY_PREFIX + "*")).thenReturn(null);
        
        System.out.println("[DEBUG_LOG] Testing cleanupExpiredTokens with null key set");

        // Act
        tokenCleanupTask.cleanupExpiredTokens();

        // Assert
        verify(redisTemplate).keys(BLACKLIST_KEY_PREFIX + "*");
        verify(jwtUtil, never()).isTokenExpired(anyString());
        verify(redisTemplate, never()).delete(anyString());
        
        System.out.println("[DEBUG_LOG] Verified that no tokens were processed when key set is null");
    }

    @Test
    void cleanupExpiredTokens_ShouldHandleRedisConnectionFailure() {
        // Arrange
        when(redisTemplate.keys(BLACKLIST_KEY_PREFIX + "*")).thenThrow(new RedisConnectionFailureException("Connection failed"));
        
        System.out.println("[DEBUG_LOG] Testing cleanupExpiredTokens with Redis connection failure");

        // Act
        tokenCleanupTask.cleanupExpiredTokens();

        // Assert
        verify(redisTemplate).keys(BLACKLIST_KEY_PREFIX + "*");
        verify(jwtUtil, never()).isTokenExpired(anyString());
        verify(redisTemplate, never()).delete(anyString());
        
        System.out.println("[DEBUG_LOG] Verified that exception was handled gracefully");
    }

    @Test
    void cleanupExpiredTokens_ShouldContinueProcessingAfterError() {
        // Arrange
        String errorToken = "error.token";
        mockKeys.add(BLACKLIST_KEY_PREFIX + errorToken);
        
        when(redisTemplate.keys(BLACKLIST_KEY_PREFIX + "*")).thenReturn(mockKeys);
        when(jwtUtil.isTokenExpired(errorToken)).thenThrow(new RuntimeException("Test exception"));
        when(redisTemplate.delete(BLACKLIST_KEY_PREFIX + expiredToken)).thenReturn(true);
        
        System.out.println("[DEBUG_LOG] Testing cleanupExpiredTokens with error during processing");

        // Act
        tokenCleanupTask.cleanupExpiredTokens();

        // Assert
        verify(redisTemplate).keys(BLACKLIST_KEY_PREFIX + "*");
        verify(jwtUtil).isTokenExpired(expiredToken);
        verify(jwtUtil).isTokenExpired(validToken);
        verify(jwtUtil).isTokenExpired(errorToken);
        verify(redisTemplate).delete(BLACKLIST_KEY_PREFIX + expiredToken);
        verify(redisTemplate, never()).delete(BLACKLIST_KEY_PREFIX + validToken);
        verify(redisTemplate, never()).delete(BLACKLIST_KEY_PREFIX + errorToken);
        
        System.out.println("[DEBUG_LOG] Verified that processing continued after error");
    }
}
