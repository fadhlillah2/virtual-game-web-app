package co.id.virtual.game.web.app.controller;

import co.id.virtual.game.web.app.dto.ApiResponse;
import co.id.virtual.game.web.app.dto.auth.AuthResponse;
import co.id.virtual.game.web.app.dto.auth.LoginRequest;
import co.id.virtual.game.web.app.dto.auth.RegisterRequest;
import co.id.virtual.game.web.app.security.UserPrincipal;
import co.id.virtual.game.web.app.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling authentication operations.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Register a new user.
     *
     * @param request the registration request
     * @return the authentication response
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(ApiResponse.success("User registered successfully", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Authenticate a user.
     *
     * @param request the login request
     * @return the authentication response
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.authenticate(request);
            return ResponseEntity.ok(ApiResponse.success("User authenticated successfully", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Refresh an authentication token.
     *
     * @param refreshToken the refresh token
     * @return the new authentication response
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestParam String refreshToken) {
        try {
            AuthResponse response = authService.refreshToken(refreshToken);
            return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Logout a user.
     *
     * @param userPrincipal the authenticated user
     * @param authorizationHeader the Authorization header containing the access token
     * @param refreshToken the refresh token to blacklist
     * @return a success response
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam(value = "refreshToken", required = false) String refreshToken) {

        String accessToken = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            accessToken = authorizationHeader.substring(7);
        }

        authService.logout(userPrincipal.getId(), accessToken, refreshToken);
        return ResponseEntity.ok(ApiResponse.success("User logged out successfully"));
    }

    /**
     * Validate a token.
     *
     * @param token the token to validate
     * @return a success response if the token is valid, an error response otherwise
     */
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validateToken(@RequestParam String token) {
        boolean isValid = authService.validateToken(token);
        if (isValid) {
            return ResponseEntity.ok(ApiResponse.success("Token is valid", true));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("Token is invalid", false));
        }
    }

    /**
     * Revoke all tokens for a user.
     * This is useful for security purposes, such as when a user changes their password
     * or when suspicious activity is detected.
     *
     * @param userPrincipal the authenticated user
     * @return a success response with the number of tokens revoked
     */
    @PostMapping("/revoke-all")
    public ResponseEntity<ApiResponse<Integer>> revokeAllTokens(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            int count = authService.revokeAllUserTokens(userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success("Revoked " + count + " tokens for user", count));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get all blacklisted tokens for a user.
     * This is mainly for administrative purposes.
     *
     * @param userPrincipal the authenticated user
     * @return a success response with the list of blacklisted tokens
     */
    @GetMapping("/blacklisted-tokens")
    public ResponseEntity<ApiResponse<List<String>>> getBlacklistedTokens(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            List<String> tokens = authService.getBlacklistedTokensForUser(userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success("Retrieved " + tokens.size() + " blacklisted tokens", tokens));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
