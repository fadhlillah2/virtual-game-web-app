package co.id.virtual.game.web.app.service.impl;

import co.id.virtual.game.web.app.dto.auth.AuthResponse;
import co.id.virtual.game.web.app.dto.auth.LoginRequest;
import co.id.virtual.game.web.app.dto.auth.RegisterRequest;
import co.id.virtual.game.web.app.dto.user.UserDto;
import co.id.virtual.game.web.app.model.User;
import co.id.virtual.game.web.app.repository.UserRepository;
import co.id.virtual.game.web.app.security.CustomUserDetailsService;
import co.id.virtual.game.web.app.security.JwtUtil;
import co.id.virtual.game.web.app.security.UserPrincipal;
import co.id.virtual.game.web.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of the AuthService interface.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Autowired
    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken: " + request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already taken: " + request.getEmail());
        }

        // Check if user is at least 18 years old
        LocalDate minAge = LocalDate.now().minusYears(18);
        if (request.getBirthDate().isAfter(minAge)) {
            throw new IllegalArgumentException("User must be at least 18 years old");
        }

        // Create new user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .birthDate(request.getBirthDate())
                .chipsBalance(1000L)
                .totalChipsWon(0L)
                .level(1)
                .experiencePoints(0)
                .isPremium(false)
                .isActive(true)
                .lastLogin(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        // Create UserPrincipal for JWT generation
        UserPrincipal userPrincipal = UserPrincipal.create(savedUser);

        // Generate JWT tokens
        String accessToken = jwtUtil.generateToken(userPrincipal);
        String refreshToken = jwtUtil.generateRefreshToken(userPrincipal);

        // Create UserDto for response
        UserDto userDto = mapUserToDto(savedUser);

        // Return AuthResponse
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtExpiration)
                .user(userDto)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse authenticate(LoginRequest request) {
        // Find user by username or email
        User user = userRepository.findByUsername(request.getEmailOrUsername())
                .orElseGet(() -> userRepository.findByEmail(request.getEmailOrUsername())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "User not found with username or email: " + request.getEmailOrUsername()))
                );

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid password");
        }

        // Create UserPrincipal for JWT generation
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        // Update last login time
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate JWT tokens
        String accessToken = jwtUtil.generateToken(userPrincipal);
        String refreshToken = jwtUtil.generateRefreshToken(userPrincipal);

        // Create UserDto for response
        UserDto userDto = mapUserToDto(user);

        // Return AuthResponse
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtExpiration)
                .user(userDto)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        // Validate refresh token
        if (!jwtUtil.validateToken(refreshToken, null)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        // Get username from token
        String username = jwtUtil.getUsernameFromToken(refreshToken);

        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Generate new access token
        String accessToken = jwtUtil.generateToken(userDetails);

        // Get user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        // Create UserDto for response
        UserDto userDto = mapUserToDto(user);

        // Return AuthResponse
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken) // Keep the same refresh token
                .expiresIn(jwtExpiration)
                .user(userDto)
                .build();
    }

    @Override
    @Transactional
    public void logout(UUID userId) {
        // In a stateless JWT-based authentication system, there's no server-side logout
        // The client should simply remove the tokens from storage

        // However, we can update the user's last login time for tracking purposes
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            return !jwtUtil.getExpirationDateFromToken(token).before(new java.util.Date());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UUID getUserIdFromToken(String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        return user.getId();
    }

    /**
     * Map a User entity to a UserDto.
     *
     * @param user the user entity
     * @return the user DTO
     */
    private UserDto mapUserToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .avatarUrl(user.getAvatarUrl())
                .chipsBalance(user.getChipsBalance())
                .totalChipsWon(user.getTotalChipsWon())
                .level(user.getLevel())
                .experiencePoints(user.getExperiencePoints())
                .isPremium(user.getIsPremium())
                .lastLogin(user.getLastLogin())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
