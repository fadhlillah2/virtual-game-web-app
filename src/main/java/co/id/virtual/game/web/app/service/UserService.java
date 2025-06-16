package co.id.virtual.game.web.app.service;

import co.id.virtual.game.web.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing users.
 */
public interface UserService {
    
    /**
     * Register a new user.
     *
     * @param user the user to register
     * @return the registered user
     * @throws IllegalArgumentException if the username or email is already taken
     */
    User registerUser(User user);
    
    /**
     * Find a user by ID.
     *
     * @param id the ID of the user to find
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findById(UUID id);
    
    /**
     * Find a user by username.
     *
     * @param username the username to search for
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find a user by email.
     *
     * @param email the email to search for
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Update a user's profile.
     *
     * @param user the user to update
     * @return the updated user
     * @throws IllegalArgumentException if the user does not exist
     */
    User updateUser(User user);
    
    /**
     * Update a user's chip balance.
     *
     * @param userId the ID of the user to update
     * @param chipsBalance the new chip balance
     * @return the updated user
     * @throws IllegalArgumentException if the user does not exist
     */
    User updateChipsBalance(UUID userId, Long chipsBalance);
    
    /**
     * Update a user's last login time.
     *
     * @param userId the ID of the user to update
     * @param lastLogin the new last login time
     * @return the updated user
     * @throws IllegalArgumentException if the user does not exist
     */
    User updateLastLogin(UUID userId, LocalDateTime lastLogin);
    
    /**
     * Find users with the highest chip balances.
     *
     * @param pageable pagination information
     * @return a page of users with the highest chip balances
     */
    Page<User> findTopUsersByChipsBalance(Pageable pageable);
    
    /**
     * Check if a username exists.
     *
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if an email exists.
     *
     * @param email the email to check
     * @return true if the email exists, false otherwise
     */
    boolean existsByEmail(String email);
}
