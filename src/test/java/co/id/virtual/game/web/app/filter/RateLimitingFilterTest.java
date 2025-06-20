package co.id.virtual.game.web.app.filter;

import co.id.virtual.game.web.app.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RRateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RateLimitingFilterTest {

    @Mock
    private RRateLimiter loginRateLimiter;

    @Mock
    private RRateLimiter registerRateLimiter;

    @Mock
    private RRateLimiter refreshTokenRateLimiter;

    @Mock
    private RRateLimiter dailyBonusRateLimiter;

    @Mock
    private RRateLimiter giftRateLimiter;

    @Mock
    private RRateLimiter profileUpdateRateLimiter;

    @Mock
    private RRateLimiter gameSessionJoinRateLimiter;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private RateLimitingFilter rateLimitingFilter;

    @BeforeEach
    void setUp() {
        // Reset mocks
        reset(loginRateLimiter, registerRateLimiter, refreshTokenRateLimiter, 
              dailyBonusRateLimiter, giftRateLimiter, profileUpdateRateLimiter, gameSessionJoinRateLimiter,
              objectMapper, request, response, filterChain);
    }

    @Test
    void shouldAllowRequestWhenPathIsNotRateLimited() throws Exception {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/some-other-endpoint");

        // Act
        rateLimitingFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(loginRateLimiter, registerRateLimiter, refreshTokenRateLimiter);
    }

    @Test
    void shouldAllowRequestWhenRateLimitNotExceeded() throws Exception {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/auth/login");
        when(loginRateLimiter.tryAcquire()).thenReturn(true);

        // Act
        rateLimitingFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(loginRateLimiter).tryAcquire();
    }

    @Test
    void shouldBlockRequestWhenRateLimitExceeded() throws Exception {
        // Create a new instance of the filter for this test to avoid issues with mocks
        RateLimitingFilter testFilter = new RateLimitingFilter(
            loginRateLimiter, registerRateLimiter, refreshTokenRateLimiter, 
            dailyBonusRateLimiter, giftRateLimiter, profileUpdateRateLimiter, gameSessionJoinRateLimiter,
            objectMapper);

        // Arrange
        when(request.getRequestURI()).thenReturn("/api/auth/login");
        when(loginRateLimiter.tryAcquire()).thenReturn(false);

        // Mock the response output stream
        when(response.getOutputStream()).thenReturn(new jakarta.servlet.ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Do nothing
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(jakarta.servlet.WriteListener writeListener) {
                // Do nothing
            }
        });

        // Act
        testFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(loginRateLimiter).tryAcquire();
        verify(response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(objectMapper).writeValue(any(jakarta.servlet.ServletOutputStream.class), any(ApiResponse.class));
        verifyNoInteractions(filterChain);
    }

    @Test
    void shouldUseCorrectRateLimiterForEachEndpoint() throws Exception {
        // Create a new instance of the filter for this test to avoid issues with mocks
        RateLimitingFilter testFilter = new RateLimitingFilter(
            loginRateLimiter, registerRateLimiter, refreshTokenRateLimiter, 
            dailyBonusRateLimiter, giftRateLimiter, profileUpdateRateLimiter, gameSessionJoinRateLimiter,
            objectMapper);

        // Test login endpoint
        when(request.getRequestURI()).thenReturn("/api/auth/login");
        when(loginRateLimiter.tryAcquire()).thenReturn(true);
        testFilter.doFilterInternal(request, response, filterChain);
        verify(loginRateLimiter).tryAcquire();
        verify(registerRateLimiter, never()).tryAcquire();
        verify(refreshTokenRateLimiter, never()).tryAcquire();
        verify(dailyBonusRateLimiter, never()).tryAcquire();
        verify(giftRateLimiter, never()).tryAcquire();
        verify(profileUpdateRateLimiter, never()).tryAcquire();
        verify(gameSessionJoinRateLimiter, never()).tryAcquire();

        // Reset
        reset(loginRateLimiter, registerRateLimiter, refreshTokenRateLimiter, 
              dailyBonusRateLimiter, giftRateLimiter, profileUpdateRateLimiter, gameSessionJoinRateLimiter,
              filterChain);

        // Test register endpoint
        when(request.getRequestURI()).thenReturn("/api/auth/register");
        when(registerRateLimiter.tryAcquire()).thenReturn(true);
        testFilter.doFilterInternal(request, response, filterChain);
        verify(registerRateLimiter).tryAcquire();
        verify(loginRateLimiter, never()).tryAcquire();
        verify(refreshTokenRateLimiter, never()).tryAcquire();
        verify(dailyBonusRateLimiter, never()).tryAcquire();
        verify(giftRateLimiter, never()).tryAcquire();
        verify(profileUpdateRateLimiter, never()).tryAcquire();
        verify(gameSessionJoinRateLimiter, never()).tryAcquire();

        // Reset
        reset(loginRateLimiter, registerRateLimiter, refreshTokenRateLimiter, 
              dailyBonusRateLimiter, giftRateLimiter, profileUpdateRateLimiter, gameSessionJoinRateLimiter,
              filterChain);

        // Test refresh token endpoint
        when(request.getRequestURI()).thenReturn("/api/auth/refresh");
        when(refreshTokenRateLimiter.tryAcquire()).thenReturn(true);
        testFilter.doFilterInternal(request, response, filterChain);
        verify(refreshTokenRateLimiter).tryAcquire();
        verify(loginRateLimiter, never()).tryAcquire();
        verify(registerRateLimiter, never()).tryAcquire();
        verify(dailyBonusRateLimiter, never()).tryAcquire();
        verify(giftRateLimiter, never()).tryAcquire();
        verify(profileUpdateRateLimiter, never()).tryAcquire();
        verify(gameSessionJoinRateLimiter, never()).tryAcquire();
    }

    @Test
    void shouldHandleClientIpFromXForwardedForHeader() throws Exception {
        // Create a new instance of the filter for this test to avoid issues with mocks
        RateLimitingFilter testFilter = new RateLimitingFilter(
            loginRateLimiter, registerRateLimiter, refreshTokenRateLimiter, 
            dailyBonusRateLimiter, giftRateLimiter, profileUpdateRateLimiter, gameSessionJoinRateLimiter,
            objectMapper);

        // Arrange
        when(request.getRequestURI()).thenReturn("/api/auth/login");
        when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.1.1, 10.0.0.1");
        when(loginRateLimiter.tryAcquire()).thenReturn(true);

        // Act
        testFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(loginRateLimiter).tryAcquire();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldRateLimitDailyBonusEndpoint() throws Exception {
        // Create a new instance of the filter for this test to avoid issues with mocks
        RateLimitingFilter testFilter = new RateLimitingFilter(
            loginRateLimiter, registerRateLimiter, refreshTokenRateLimiter, 
            dailyBonusRateLimiter, giftRateLimiter, profileUpdateRateLimiter, gameSessionJoinRateLimiter,
            objectMapper);

        // Arrange
        when(request.getRequestURI()).thenReturn("/api/chips/daily-bonus");
        when(dailyBonusRateLimiter.tryAcquire()).thenReturn(true);

        // Act
        testFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(dailyBonusRateLimiter).tryAcquire();
        verify(loginRateLimiter, never()).tryAcquire();
        verify(registerRateLimiter, never()).tryAcquire();
        verify(refreshTokenRateLimiter, never()).tryAcquire();
        verify(giftRateLimiter, never()).tryAcquire();
        verify(profileUpdateRateLimiter, never()).tryAcquire();
        verify(gameSessionJoinRateLimiter, never()).tryAcquire();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldRateLimitGiftEndpoint() throws Exception {
        // Create a new instance of the filter for this test to avoid issues with mocks
        RateLimitingFilter testFilter = new RateLimitingFilter(
            loginRateLimiter, registerRateLimiter, refreshTokenRateLimiter, 
            dailyBonusRateLimiter, giftRateLimiter, profileUpdateRateLimiter, gameSessionJoinRateLimiter,
            objectMapper);

        // Arrange
        when(request.getRequestURI()).thenReturn("/api/chips/gift");
        when(giftRateLimiter.tryAcquire()).thenReturn(true);

        // Act
        testFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(giftRateLimiter).tryAcquire();
        verify(loginRateLimiter, never()).tryAcquire();
        verify(registerRateLimiter, never()).tryAcquire();
        verify(refreshTokenRateLimiter, never()).tryAcquire();
        verify(dailyBonusRateLimiter, never()).tryAcquire();
        verify(profileUpdateRateLimiter, never()).tryAcquire();
        verify(gameSessionJoinRateLimiter, never()).tryAcquire();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldRateLimitProfileUpdateEndpoint() throws Exception {
        // Create a new instance of the filter for this test to avoid issues with mocks
        RateLimitingFilter testFilter = new RateLimitingFilter(
            loginRateLimiter, registerRateLimiter, refreshTokenRateLimiter, 
            dailyBonusRateLimiter, giftRateLimiter, profileUpdateRateLimiter, gameSessionJoinRateLimiter,
            objectMapper);

        // Arrange
        when(request.getRequestURI()).thenReturn("/api/users/me");
        when(request.getMethod()).thenReturn("PUT");
        when(profileUpdateRateLimiter.tryAcquire()).thenReturn(true);

        // Act
        testFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(profileUpdateRateLimiter).tryAcquire();
        verify(loginRateLimiter, never()).tryAcquire();
        verify(registerRateLimiter, never()).tryAcquire();
        verify(refreshTokenRateLimiter, never()).tryAcquire();
        verify(dailyBonusRateLimiter, never()).tryAcquire();
        verify(giftRateLimiter, never()).tryAcquire();
        verify(gameSessionJoinRateLimiter, never()).tryAcquire();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotRateLimitProfileGetEndpoint() throws Exception {
        // Create a new instance of the filter for this test to avoid issues with mocks
        RateLimitingFilter testFilter = new RateLimitingFilter(
            loginRateLimiter, registerRateLimiter, refreshTokenRateLimiter, 
            dailyBonusRateLimiter, giftRateLimiter, profileUpdateRateLimiter, gameSessionJoinRateLimiter,
            objectMapper);

        // Arrange
        when(request.getRequestURI()).thenReturn("/api/users/me");
        when(request.getMethod()).thenReturn("GET");

        // Act
        testFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(profileUpdateRateLimiter, never()).tryAcquire();
        verify(loginRateLimiter, never()).tryAcquire();
        verify(registerRateLimiter, never()).tryAcquire();
        verify(refreshTokenRateLimiter, never()).tryAcquire();
        verify(dailyBonusRateLimiter, never()).tryAcquire();
        verify(giftRateLimiter, never()).tryAcquire();
        verify(gameSessionJoinRateLimiter, never()).tryAcquire();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldRateLimitGameSessionJoinEndpoint() throws Exception {
        // Create a new instance of the filter for this test to avoid issues with mocks
        RateLimitingFilter testFilter = new RateLimitingFilter(
            loginRateLimiter, registerRateLimiter, refreshTokenRateLimiter, 
            dailyBonusRateLimiter, giftRateLimiter, profileUpdateRateLimiter, gameSessionJoinRateLimiter,
            objectMapper);

        // Arrange
        when(request.getRequestURI()).thenReturn("/api/games/sessions/join");
        when(gameSessionJoinRateLimiter.tryAcquire()).thenReturn(true);

        // Act
        testFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(gameSessionJoinRateLimiter).tryAcquire();
        verify(loginRateLimiter, never()).tryAcquire();
        verify(registerRateLimiter, never()).tryAcquire();
        verify(refreshTokenRateLimiter, never()).tryAcquire();
        verify(dailyBonusRateLimiter, never()).tryAcquire();
        verify(giftRateLimiter, never()).tryAcquire();
        verify(profileUpdateRateLimiter, never()).tryAcquire();
        verify(filterChain).doFilter(request, response);
    }
}
