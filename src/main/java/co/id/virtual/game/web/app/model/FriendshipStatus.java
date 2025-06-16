package co.id.virtual.game.web.app.model;

/**
 * Enum representing the status of a friendship.
 */
public enum FriendshipStatus {
    /**
     * The friend request is pending acceptance.
     */
    PENDING,
    
    /**
     * The friend request has been accepted.
     */
    ACCEPTED,
    
    /**
     * The friend request has been rejected.
     */
    REJECTED,
    
    /**
     * The friendship has been blocked.
     */
    BLOCKED
}
