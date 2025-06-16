package co.id.virtual.game.web.app.repository;

import co.id.virtual.game.web.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * Repository interface for User entity.
 * Provides methods to access and manipulate user data.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

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

    /**
     * Update a user's chip balance.
     *
     * @param userId the ID of the user to update
     * @param chipsBalance the new chip balance
     * @return the number of rows affected
     */
    @Modifying
    @Query("UPDATE User u SET u.chipsBalance = :chipsBalance WHERE u.id = :userId")
    int updateChipsBalance(@Param("userId") UUID userId, @Param("chipsBalance") Long chipsBalance);

    /**
     * Update a user's last login time.
     *
     * @param userId the ID of the user to update
     * @param lastLogin the new last login time
     * @return the number of rows affected
     */
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.id = :userId")
    int updateLastLogin(@Param("userId") UUID userId, @Param("lastLogin") LocalDateTime lastLogin);

    /**
     * Find users with the highest chip balances.
     *
     * @param pageable pagination information
     * @return a list of users with the highest chip balances
     */
    @Query("SELECT u FROM User u ORDER BY u.chipsBalance DESC")
    List<User> findTopUsersByChipsBalance(Pageable pageable);
}
