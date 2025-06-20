package co.id.virtual.game.web.app.filter;

import co.id.virtual.game.web.app.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.redisson.api.RRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

/**
 * Filter for rate limiting authentication endpoints and sensitive operations.
 * This filter applies rate limits to prevent brute force attacks and abuse.
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RRateLimiter loginRateLimiter;
    private final RRateLimiter registerRateLimiter;
    private final RRateLimiter refreshTokenRateLimiter;
    private final RRateLimiter dailyBonusRateLimiter;
    private final RRateLimiter giftRateLimiter;
    private final RRateLimiter profileUpdateRateLimiter;
    private final RRateLimiter gameSessionJoinRateLimiter;
    private final ObjectMapper objectMapper;

    // Map of endpoints to their respective rate limiters
    private final Map<String, RRateLimiter> endpointRateLimiters;

    @Autowired
    public RateLimitingFilter(
            RRateLimiter loginRateLimiter,
            RRateLimiter registerRateLimiter,
            RRateLimiter refreshTokenRateLimiter,
            RRateLimiter dailyBonusRateLimiter,
            RRateLimiter giftRateLimiter,
            RRateLimiter profileUpdateRateLimiter,
            RRateLimiter gameSessionJoinRateLimiter,
            ObjectMapper objectMapper) {
        this.loginRateLimiter = loginRateLimiter;
        this.registerRateLimiter = registerRateLimiter;
        this.refreshTokenRateLimiter = refreshTokenRateLimiter;
        this.dailyBonusRateLimiter = dailyBonusRateLimiter;
        this.giftRateLimiter = giftRateLimiter;
        this.profileUpdateRateLimiter = profileUpdateRateLimiter;
        this.gameSessionJoinRateLimiter = gameSessionJoinRateLimiter;
        this.objectMapper = objectMapper;

        // Initialize the map of endpoints to rate limiters
        this.endpointRateLimiters = Map.of(
                // Authentication endpoints
                "/api/auth/login", loginRateLimiter,
                "/api/auth/register", registerRateLimiter,
                "/api/auth/refresh", refreshTokenRateLimiter,

                // Sensitive operations
                "/api/chips/daily-bonus", dailyBonusRateLimiter,
                "/api/chips/gift", giftRateLimiter,
                "/api/users/me", profileUpdateRateLimiter,
                "/api/games/sessions/join", gameSessionJoinRateLimiter
        );
    }

    /**
     * Gets the map of endpoints to rate limiters.
     * This method is primarily for testing purposes.
     *
     * @return the map of endpoints to rate limiters
     */
    protected Map<String, RRateLimiter> getEndpointRateLimiters() {
        return endpointRateLimiters;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Get the request path and method
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Special handling for endpoints that need method-specific rate limiting
        if (path.equals("/api/users/me") && method.equals("PUT")) {
            // Only rate limit PUT requests to /api/users/me (profile updates)
            applyRateLimit(request, response, filterChain, profileUpdateRateLimiter, path);
            return;
        }

        // Check if the path is one of the rate-limited endpoints
        RRateLimiter rateLimiter = endpointRateLimiters.get(path);
        if (rateLimiter != null) {
            applyRateLimit(request, response, filterChain, rateLimiter, path);
            return;
        }

        // Continue with the filter chain if no rate limiting is applied
        filterChain.doFilter(request, response);
    }

    /**
     * Applies rate limiting for a specific endpoint.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @param rateLimiter the rate limiter to use
     * @param path the request path
     * @throws ServletException if a servlet exception occurs
     * @throws IOException if an I/O exception occurs
     */
    private void applyRateLimit(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain,
            RRateLimiter rateLimiter,
            String path) throws ServletException, IOException {

        // Get client IP for rate limiting key
        String clientIp = getClientIp(request);
        String rateLimitKey = path + ":" + clientIp;

        // Try to acquire a permit from the rate limiter
        if (!rateLimiter.tryAcquire()) {
            // If rate limit exceeded, return 429 Too Many Requests
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            // Create error response
            ApiResponse<Void> errorResponse = ApiResponse.error(
                    "Rate limit exceeded. Please try again later.");

            // Write error response to output
            objectMapper.writeValue(response.getOutputStream(), errorResponse);
            return;
        }

        // Continue with the filter chain if rate limit not exceeded
        filterChain.doFilter(request, response);
    }

    /**
     * Gets the client IP address from the request.
     * Handles cases where the request might be coming through a proxy.
     *
     * @param request the HTTP request
     * @return the client IP address
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // X-Forwarded-For can contain multiple IPs, take the first one
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
