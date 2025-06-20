package co.id.virtual.game.web.app.service;

import co.id.virtual.game.web.app.model.User;
import co.id.virtual.game.web.app.repository.UserRepository;
import co.id.virtual.game.web.app.security.CustomUserDetailsService;
import co.id.virtual.game.web.app.security.JwtUtil;
import co.id.virtual.game.web.app.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the AuthService implementation.
 */
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;
    private UUID userId;
    private String accessToken;
    private String refreshToken;

    @BeforeEach
    void setUp() {
        // Create a test user
        userId = UUID.randomUUID();
        testUser = User.builder()
                .id(userId)
                .username("testuser")
                .email("test@example.com")
                .passwordHash("hashedpassword")
                .birthDate(LocalDate.of(1990, 1, 1))
                .chipsBalance(1000L)
                .totalChipsWon(500L)
                .lastLogin(LocalDateTime.now())
                .build();

        // Mock tokens
        accessToken = "test.access.token";
        refreshToken = "test.refresh.token";

        // Mock repository methods
        Mockito.lenient().when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
    }

    @Test
    void logout_ShouldBlacklistBothTokens() {
        // Arrange
        System.out.println("[DEBUG_LOG] Testing logout with user ID: " + userId);
        System.out.println("[DEBUG_LOG] Access token: " + accessToken);
        System.out.println("[DEBUG_LOG] Refresh token: " + refreshToken);

        // Act
        authService.logout(userId, accessToken, refreshToken);

        // Assert
        verify(jwtUtil).blacklistToken(accessToken);
        verify(jwtUtil).blacklistToken(refreshToken);
        verify(userRepository).save(testUser);

        System.out.println("[DEBUG_LOG] Verified that both tokens were blacklisted");
    }

    @Test
    void logout_ShouldHandleNullTokens() {
        // Arrange
        System.out.println("[DEBUG_LOG] Testing logout with null tokens");

        // Act
        authService.logout(userId, null, null);

        // Assert
        verify(jwtUtil, never()).blacklistToken(anyString());
        verify(userRepository).save(testUser);

        System.out.println("[DEBUG_LOG] Verified that no tokens were blacklisted when tokens are null");
    }

    @Test
    void logout_ShouldHandleEmptyTokens() {
        // Arrange
        System.out.println("[DEBUG_LOG] Testing logout with empty tokens");

        // Act
        authService.logout(userId, "", "");

        // Assert
        verify(jwtUtil, never()).blacklistToken(anyString());
        verify(userRepository).save(testUser);

        System.out.println("[DEBUG_LOG] Verified that no tokens were blacklisted when tokens are empty");
    }

    @Test
    void logout_ShouldHandleNullAccessToken() {
        // Arrange
        System.out.println("[DEBUG_LOG] Testing logout with null access token");

        // Act
        authService.logout(userId, null, refreshToken);

        // Assert
        verify(jwtUtil, never()).blacklistToken(eq(accessToken));
        verify(jwtUtil).blacklistToken(refreshToken);
        verify(userRepository).save(testUser);

        System.out.println("[DEBUG_LOG] Verified that only refresh token was blacklisted");
    }

    @Test
    void logout_ShouldHandleNullRefreshToken() {
        // Arrange
        System.out.println("[DEBUG_LOG] Testing logout with null refresh token");

        // Act
        authService.logout(userId, accessToken, null);

        // Assert
        verify(jwtUtil).blacklistToken(accessToken);
        verify(jwtUtil, never()).blacklistToken(eq(refreshToken));
        verify(userRepository).save(testUser);

        System.out.println("[DEBUG_LOG] Verified that only access token was blacklisted");
    }

    @Test
    void revokeAllUserTokens_ShouldRevokeTokensAndUpdateLastLogin() {
        // Arrange
        System.out.println("[DEBUG_LOG] Testing revokeAllUserTokens");
        when(jwtUtil.blacklistAllUserTokens(userId)).thenReturn(5);

        // Act
        int count = authService.revokeAllUserTokens(userId);

        // Assert
        assertEquals(5, count);
        verify(jwtUtil).blacklistAllUserTokens(userId);
        verify(userRepository).save(testUser);

        System.out.println("[DEBUG_LOG] Verified that all tokens were revoked and last login was updated");
    }

    @Test
    void revokeAllUserTokens_ShouldThrowExceptionForNonExistentUser() {
        // Arrange
        System.out.println("[DEBUG_LOG] Testing revokeAllUserTokens with non-existent user");
        UUID nonExistentUserId = UUID.randomUUID();
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            authService.revokeAllUserTokens(nonExistentUserId);
        });

        verify(jwtUtil, never()).blacklistAllUserTokens(any(UUID.class));

        System.out.println("[DEBUG_LOG] Verified that exception is thrown for non-existent user");
    }

    @Test
    void getBlacklistedTokensForUser_ShouldReturnTokens() {
        // Arrange
        System.out.println("[DEBUG_LOG] Testing getBlacklistedTokensForUser");
        List<String> expectedTokens = Arrays.asList("token1", "token2", "token3");
        when(userRepository.existsById(userId)).thenReturn(true);
        when(jwtUtil.getBlacklistedTokensForUser(userId)).thenReturn(expectedTokens);

        // Act
        List<String> actualTokens = authService.getBlacklistedTokensForUser(userId);

        // Assert
        assertEquals(expectedTokens, actualTokens);
        verify(jwtUtil).getBlacklistedTokensForUser(userId);

        System.out.println("[DEBUG_LOG] Verified that blacklisted tokens were returned");
    }

    @Test
    void getBlacklistedTokensForUser_ShouldThrowExceptionForNonExistentUser() {
        // Arrange
        System.out.println("[DEBUG_LOG] Testing getBlacklistedTokensForUser with non-existent user");
        UUID nonExistentUserId = UUID.randomUUID();
        when(userRepository.existsById(nonExistentUserId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            authService.getBlacklistedTokensForUser(nonExistentUserId);
        });

        verify(jwtUtil, never()).getBlacklistedTokensForUser(any(UUID.class));

        System.out.println("[DEBUG_LOG] Verified that exception is thrown for non-existent user");
    }
}
