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
     * @return a success response
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        authService.logout(userPrincipal.getId());
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
}
