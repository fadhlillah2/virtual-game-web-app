package co.id.virtual.game.web.app.service;

import co.id.virtual.game.web.app.dto.auth.AuthResponse;
import co.id.virtual.game.web.app.dto.auth.LoginRequest;
import co.id.virtual.game.web.app.dto.auth.RegisterRequest;

import java.util.UUID;

/**
 * Service interface for authentication operations.
 */
public interface AuthService {
    
    /**
     * Register a new user.
     *
     * @param request the registration request
     * @return the authentication response
     * @throws IllegalArgumentException if the username or email is already taken
     */
    AuthResponse register(RegisterRequest request);
    
    /**
     * Authenticate a user.
     *
     * @param request the login request
     * @return the authentication response
     * @throws IllegalArgumentException if the credentials are invalid
     */
    AuthResponse authenticate(LoginRequest request);
    
    /**
     * Refresh an authentication token.
     *
     * @param refreshToken the refresh token
     * @return the new authentication response
     * @throws IllegalArgumentException if the refresh token is invalid
     */
    AuthResponse refreshToken(String refreshToken);
    
    /**
     * Logout a user.
     *
     * @param userId the ID of the user to logout
     */
    void logout(UUID userId);
    
    /**
     * Validate a token.
     *
     * @param token the token to validate
     * @return true if the token is valid, false otherwise
     */
    boolean validateToken(String token);
    
    /**
     * Get the user ID from a token.
     *
     * @param token the token
     * @return the user ID
     * @throws IllegalArgumentException if the token is invalid
     */
    UUID getUserIdFromToken(String token);
}
