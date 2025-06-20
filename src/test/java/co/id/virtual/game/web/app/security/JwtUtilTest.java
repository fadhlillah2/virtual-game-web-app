package co.id.virtual.game.web.app.security;

import co.id.virtual.game.web.app.repository.BlacklistedTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {

    @Mock
    private BlacklistedTokenRepository blacklistedTokenRepository;

    @Mock
    private UserDetails userDetails;

    private JwtUtil jwtUtil;
    private String testToken;

    @BeforeEach
    void setUp() {
        // Create JwtUtil with mocked BlacklistedTokenRepository
        jwtUtil = new JwtUtil(blacklistedTokenRepository);

        // Set required properties using reflection
        // The secret key needs to be at least 512 bits (64 bytes) for HS512
        String secretKey = "test-secret-key-for-jwt-token-that-is-long-enough-for-hs512-algorithm-with-minimum-512-bits-length-requirement-satisfied";
        ReflectionTestUtils.setField(jwtUtil, "secretString", secretKey);
        ReflectionTestUtils.setField(jwtUtil, "expiration", 3600000L); // 1 hour
        ReflectionTestUtils.setField(jwtUtil, "refreshExpiration", 86400000L); // 24 hours

        // Initialize the secret key
        jwtUtil.init();

        // Mock UserDetails
        when(userDetails.getUsername()).thenReturn("testuser");

        // Generate a test token
        testToken = jwtUtil.generateToken(userDetails);

        System.out.println("[DEBUG_LOG] Test token generated: " + testToken);
    }

    @Test
    void validateToken_ShouldReturnTrue_WhenTokenIsValidAndNotBlacklisted() {
        // Arrange
        when(blacklistedTokenRepository.isTokenBlacklisted(testToken)).thenReturn(false);

        // Act
        boolean result = jwtUtil.validateToken(testToken, userDetails);

        // Assert
        assertTrue(result);
        verify(blacklistedTokenRepository).isTokenBlacklisted(testToken);

        System.out.println("[DEBUG_LOG] Token validation result: " + result);
    }

    @Test
    void validateToken_ShouldReturnFalse_WhenTokenIsBlacklisted() {
        // Arrange
        when(blacklistedTokenRepository.isTokenBlacklisted(testToken)).thenReturn(true);

        // Act
        boolean result = jwtUtil.validateToken(testToken, userDetails);

        // Assert
        assertFalse(result);
        verify(blacklistedTokenRepository).isTokenBlacklisted(testToken);

        System.out.println("[DEBUG_LOG] Token validation result for blacklisted token: " + result);
    }

    @Test
    void blacklistToken_ShouldCallRepository() {
        // Act
        jwtUtil.blacklistToken(testToken);

        // Assert
        verify(blacklistedTokenRepository).blacklistToken(eq(testToken), anyLong());

        System.out.println("[DEBUG_LOG] Token blacklisted in test");
    }

    @Test
    void validateToken_ShouldHandleExceptions() {
        // Arrange
        when(blacklistedTokenRepository.isTokenBlacklisted(anyString())).thenThrow(new RuntimeException("Test exception"));

        // Act
        boolean result = jwtUtil.validateToken("invalid.token", userDetails);

        // Assert
        assertFalse(result);

        System.out.println("[DEBUG_LOG] Token validation with exception: " + result);
    }
}
