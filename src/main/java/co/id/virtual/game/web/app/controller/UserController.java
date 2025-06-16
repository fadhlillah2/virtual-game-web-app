package co.id.virtual.game.web.app.controller;

import co.id.virtual.game.web.app.dto.ApiResponse;
import co.id.virtual.game.web.app.dto.user.UserDto;
import co.id.virtual.game.web.app.model.User;
import co.id.virtual.game.web.app.security.UserPrincipal;
import co.id.virtual.game.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller for handling user operations.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Get the current user's profile.
     *
     * @param userPrincipal the authenticated user
     * @return the user profile
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userService.findById(userPrincipal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        UserDto userDto = mapUserToDto(user);
        return ResponseEntity.ok(ApiResponse.success("User profile retrieved successfully", userDto));
    }
    
    /**
     * Get a user by ID.
     *
     * @param id the user ID
     * @return the user profile
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable UUID id) {
        try {
            User user = userService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
            
            UserDto userDto = mapUserToDto(user);
            return ResponseEntity.ok(ApiResponse.success("User profile retrieved successfully", userDto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Update the current user's profile.
     *
     * @param userPrincipal the authenticated user
     * @param userDto the updated user profile
     * @return the updated user profile
     */
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> updateCurrentUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody UserDto userDto) {
        try {
            User user = userService.findById(userPrincipal.getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            
            // Update user fields (only allowed fields)
            user.setAvatarUrl(userDto.getAvatarUrl());
            
            User updatedUser = userService.updateUser(user);
            UserDto updatedUserDto = mapUserToDto(updatedUser);
            
            return ResponseEntity.ok(ApiResponse.success("User profile updated successfully", updatedUserDto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
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
