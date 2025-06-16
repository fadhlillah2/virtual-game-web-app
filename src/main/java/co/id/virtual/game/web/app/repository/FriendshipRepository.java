package co.id.virtual.game.web.app.repository;

import co.id.virtual.game.web.app.model.Friendship;
import co.id.virtual.game.web.app.model.FriendshipStatus;
import co.id.virtual.game.web.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Friendship entities.
 */
@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {
    
    /**
     * Find a friendship between two users.
     *
     * @param user1 the first user
     * @param user2 the second user
     * @return the friendship, if it exists
     */
    @Query("SELECT f FROM Friendship f WHERE " +
            "(f.requester = :user1 AND f.addressee = :user2) OR " +
            "(f.requester = :user2 AND f.addressee = :user1)")
    Optional<Friendship> findByUsers(@Param("user1") User user1, @Param("user2") User user2);
    
    /**
     * Find all friendships where the given user is either the requester or the addressee.
     *
     * @param user the user
     * @return the list of friendships
     */
    @Query("SELECT f FROM Friendship f WHERE " +
            "f.requester = :user OR f.addressee = :user")
    List<Friendship> findByUser(@Param("user") User user);
    
    /**
     * Find all friendships where the given user is either the requester or the addressee,
     * and the friendship has the given status.
     *
     * @param user the user
     * @param status the friendship status
     * @return the list of friendships
     */
    @Query("SELECT f FROM Friendship f WHERE " +
            "(f.requester = :user OR f.addressee = :user) AND " +
            "f.status = :status")
    List<Friendship> findByUserAndStatus(@Param("user") User user, @Param("status") FriendshipStatus status);
    
    /**
     * Find all friendships where the given user is the addressee,
     * and the friendship has the given status.
     *
     * @param user the user
     * @param status the friendship status
     * @return the list of friendships
     */
    List<Friendship> findByAddresseeAndStatus(User user, FriendshipStatus status);
    
    /**
     * Find all friendships where the given user is the requester,
     * and the friendship has the given status.
     *
     * @param user the user
     * @param status the friendship status
     * @return the list of friendships
     */
    List<Friendship> findByRequesterAndStatus(User user, FriendshipStatus status);
    
    /**
     * Count the number of pending friend requests for the given user.
     *
     * @param user the user
     * @return the number of pending friend requests
     */
    long countByAddresseeAndStatus(User user, FriendshipStatus status);
}
