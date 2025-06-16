package co.id.virtual.game.web.app.service;

import co.id.virtual.game.web.app.dto.friend.FriendDto;
import co.id.virtual.game.web.app.model.Friendship;
import co.id.virtual.game.web.app.model.User;

import java.util.List;
import java.util.UUID;

/**
 * Service for handling friendship operations.
 */
public interface FriendService {
    
    /**
     * Get all friends of the given user.
     *
     * @param userId the user ID
     * @return the list of friends
     */
    List<FriendDto> getFriends(UUID userId);
    
    /**
     * Get all pending friend requests for the given user.
     *
     * @param userId the user ID
     * @return the list of pending friend requests
     */
    List<FriendDto> getPendingRequests(UUID userId);
    
    /**
     * Send a friend request from the current user to the given user.
     *
     * @param requesterId the requester user ID
     * @param username the username of the user to add as a friend
     * @return the created friendship
     * @throws IllegalArgumentException if the user is not found or is already a friend
     */
    Friendship sendFriendRequest(UUID requesterId, String username);
    
    /**
     * Accept a friend request.
     *
     * @param userId the user ID
     * @param friendshipId the friendship ID
     * @return the updated friendship
     * @throws IllegalArgumentException if the friendship is not found or the user is not the addressee
     */
    Friendship acceptFriendRequest(UUID userId, UUID friendshipId);
    
    /**
     * Reject a friend request.
     *
     * @param userId the user ID
     * @param friendshipId the friendship ID
     * @return the updated friendship
     * @throws IllegalArgumentException if the friendship is not found or the user is not the addressee
     */
    Friendship rejectFriendRequest(UUID userId, UUID friendshipId);
    
    /**
     * Remove a friend.
     *
     * @param userId the user ID
     * @param friendId the friend ID
     * @throws IllegalArgumentException if the friendship is not found
     */
    void removeFriend(UUID userId, UUID friendId);
    
    /**
     * Block a user.
     *
     * @param userId the user ID
     * @param blockedUserId the ID of the user to block
     * @return the updated or created friendship
     */
    Friendship blockUser(UUID userId, UUID blockedUserId);
    
    /**
     * Unblock a user.
     *
     * @param userId the user ID
     * @param blockedUserId the ID of the user to unblock
     * @throws IllegalArgumentException if the friendship is not found or the user is not blocked
     */
    void unblockUser(UUID userId, UUID blockedUserId);
    
    /**
     * Check if two users are friends.
     *
     * @param userId1 the first user ID
     * @param userId2 the second user ID
     * @return true if the users are friends, false otherwise
     */
    boolean areFriends(UUID userId1, UUID userId2);
    
    /**
     * Convert a User to a FriendDto.
     *
     * @param user the user
     * @param friendship the friendship
     * @param currentUserId the current user ID
     * @return the friend DTO
     */
    FriendDto convertToFriendDto(User user, Friendship friendship, UUID currentUserId);
}
