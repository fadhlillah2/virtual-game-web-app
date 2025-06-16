package co.id.virtual.game.web.app.service.impl;

import co.id.virtual.game.web.app.dto.friend.FriendDto;
import co.id.virtual.game.web.app.model.Friendship;
import co.id.virtual.game.web.app.model.FriendshipStatus;
import co.id.virtual.game.web.app.model.User;
import co.id.virtual.game.web.app.repository.FriendshipRepository;
import co.id.virtual.game.web.app.repository.UserRepository;
import co.id.virtual.game.web.app.service.FriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the FriendService interface.
 */
@Service
@Slf4j
public class FriendServiceImpl implements FriendService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    @Autowired
    public FriendServiceImpl(FriendshipRepository friendshipRepository, UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get all friends of the given user.
     *
     * @param userId the user ID
     * @return the list of friends
     */
    @Override
    public List<FriendDto> getFriends(UUID userId) {
        User user = getUserById(userId);
        List<Friendship> friendships = friendshipRepository.findByUserAndStatus(user, FriendshipStatus.ACCEPTED);

        return friendships.stream()
                .map(friendship -> {
                    User friend = friendship.getRequester().getId().equals(userId) ?
                            friendship.getAddressee() : friendship.getRequester();
                    return convertToFriendDto(friend, friendship, userId);
                })
                .collect(Collectors.toList());
    }

    /**
     * Get all pending friend requests for the given user.
     *
     * @param userId the user ID
     * @return the list of pending friend requests
     */
    @Override
    public List<FriendDto> getPendingRequests(UUID userId) {
        User user = getUserById(userId);
        List<Friendship> pendingRequests = friendshipRepository.findByAddresseeAndStatus(user, FriendshipStatus.PENDING);

        return pendingRequests.stream()
                .map(friendship -> convertToFriendDto(friendship.getRequester(), friendship, userId))
                .collect(Collectors.toList());
    }

    /**
     * Send a friend request from the current user to the given user.
     *
     * @param requesterId the requester user ID
     * @param username the username of the user to add as a friend
     * @return the created friendship
     * @throws IllegalArgumentException if the user is not found or is already a friend
     */
    @Override
    @Transactional
    public Friendship sendFriendRequest(UUID requesterId, String username) {
        User requester = getUserById(requesterId);
        User addressee = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        // Check if users are already friends or have a pending request
        Optional<Friendship> existingFriendship = friendshipRepository.findByUsers(requester, addressee);
        if (existingFriendship.isPresent()) {
            Friendship friendship = existingFriendship.get();
            FriendshipStatus status = friendship.getStatus();

            if (status == FriendshipStatus.ACCEPTED) {
                throw new IllegalArgumentException("Users are already friends");
            } else if (status == FriendshipStatus.PENDING) {
                if (friendship.getRequester().getId().equals(requesterId)) {
                    throw new IllegalArgumentException("Friend request already sent");
                } else {
                    // If the addressee sent a request to the requester, accept it
                    friendship.setStatus(FriendshipStatus.ACCEPTED);
                    return friendshipRepository.save(friendship);
                }
            } else if (status == FriendshipStatus.BLOCKED) {
                throw new IllegalArgumentException("Cannot send friend request to this user");
            } else {
                // If the request was rejected, create a new one
                friendship.setStatus(FriendshipStatus.PENDING);
                friendship.setRequester(requester);
                friendship.setAddressee(addressee);
                return friendshipRepository.save(friendship);
            }
        }

        // Create a new friendship
        Friendship friendship = Friendship.builder()
                .requester(requester)
                .addressee(addressee)
                .status(FriendshipStatus.PENDING)
                .build();

        return friendshipRepository.save(friendship);
    }

    /**
     * Accept a friend request.
     *
     * @param userId the user ID
     * @param friendshipId the friendship ID
     * @return the updated friendship
     * @throws IllegalArgumentException if the friendship is not found or the user is not the addressee
     */
    @Override
    @Transactional
    public Friendship acceptFriendRequest(UUID userId, UUID friendshipId) {
        Friendship friendship = getFriendshipById(friendshipId);

        if (!friendship.getAddressee().getId().equals(userId)) {
            throw new IllegalArgumentException("User is not the addressee of this friend request");
        }

        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalArgumentException("Friend request is not pending");
        }

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        return friendshipRepository.save(friendship);
    }

    /**
     * Reject a friend request.
     *
     * @param userId the user ID
     * @param friendshipId the friendship ID
     * @return the updated friendship
     * @throws IllegalArgumentException if the friendship is not found or the user is not the addressee
     */
    @Override
    @Transactional
    public Friendship rejectFriendRequest(UUID userId, UUID friendshipId) {
        Friendship friendship = getFriendshipById(friendshipId);

        if (!friendship.getAddressee().getId().equals(userId)) {
            throw new IllegalArgumentException("User is not the addressee of this friend request");
        }

        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalArgumentException("Friend request is not pending");
        }

        friendship.setStatus(FriendshipStatus.REJECTED);
        return friendshipRepository.save(friendship);
    }

    /**
     * Remove a friend.
     *
     * @param userId the user ID
     * @param friendId the friend ID
     * @throws IllegalArgumentException if the friendship is not found
     */
    @Override
    @Transactional
    public void removeFriend(UUID userId, UUID friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        Optional<Friendship> friendshipOpt = friendshipRepository.findByUsers(user, friend);
        if (friendshipOpt.isEmpty()) {
            throw new IllegalArgumentException("Friendship not found");
        }

        Friendship friendship = friendshipOpt.get();
        if (friendship.getStatus() != FriendshipStatus.ACCEPTED) {
            throw new IllegalArgumentException("Users are not friends");
        }

        friendshipRepository.delete(friendship);
    }

    /**
     * Block a user.
     *
     * @param userId the user ID
     * @param blockedUserId the ID of the user to block
     * @return the updated or created friendship
     */
    @Override
    @Transactional
    public Friendship blockUser(UUID userId, UUID blockedUserId) {
        User user = getUserById(userId);
        User blockedUser = getUserById(blockedUserId);

        Optional<Friendship> existingFriendship = friendshipRepository.findByUsers(user, blockedUser);
        Friendship friendship;

        if (existingFriendship.isPresent()) {
            friendship = existingFriendship.get();
            friendship.setStatus(FriendshipStatus.BLOCKED);

            // Ensure the blocker is the requester
            if (!friendship.getRequester().getId().equals(userId)) {
                friendship.setRequester(user);
                friendship.setAddressee(blockedUser);
            }
        } else {
            friendship = Friendship.builder()
                    .requester(user)
                    .addressee(blockedUser)
                    .status(FriendshipStatus.BLOCKED)
                    .build();
        }

        return friendshipRepository.save(friendship);
    }

    /**
     * Unblock a user.
     *
     * @param userId the user ID
     * @param blockedUserId the ID of the user to unblock
     * @throws IllegalArgumentException if the friendship is not found or the user is not blocked
     */
    @Override
    @Transactional
    public void unblockUser(UUID userId, UUID blockedUserId) {
        User user = getUserById(userId);
        User blockedUser = getUserById(blockedUserId);

        Optional<Friendship> friendshipOpt = friendshipRepository.findByUsers(user, blockedUser);
        if (friendshipOpt.isEmpty()) {
            throw new IllegalArgumentException("Friendship not found");
        }

        Friendship friendship = friendshipOpt.get();
        if (friendship.getStatus() != FriendshipStatus.BLOCKED) {
            throw new IllegalArgumentException("User is not blocked");
        }

        if (!friendship.getRequester().getId().equals(userId)) {
            throw new IllegalArgumentException("User is not the blocker");
        }

        friendshipRepository.delete(friendship);
    }

    /**
     * Check if two users are friends.
     *
     * @param userId1 the first user ID
     * @param userId2 the second user ID
     * @return true if the users are friends, false otherwise
     */
    @Override
    public boolean areFriends(UUID userId1, UUID userId2) {
        User user1 = getUserById(userId1);
        User user2 = getUserById(userId2);

        Optional<Friendship> friendshipOpt = friendshipRepository.findByUsers(user1, user2);
        return friendshipOpt.isPresent() && friendshipOpt.get().getStatus() == FriendshipStatus.ACCEPTED;
    }

    /**
     * Convert a User to a FriendDto.
     *
     * @param user the user
     * @param friendship the friendship
     * @param currentUserId the current user ID
     * @return the friend DTO
     */
    @Override
    public FriendDto convertToFriendDto(User user, Friendship friendship, UUID currentUserId) {
        // Consider a user online if they've logged in within the last 15 minutes
        boolean isOnline = user.getLastLogin() != null && 
                user.getLastLogin().isAfter(LocalDateTime.now().minusMinutes(15));

        return FriendDto.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .online(isOnline)
                .lastActivity(user.getLastLogin())
                .chipsBalance(user.getChipsBalance())
                .status(friendship.getStatus().name())
                .isRequester(friendship.getRequester().getId().equals(currentUserId))
                .createdAt(friendship.getCreatedAt())
                .build();
    }

    /**
     * Get a user by ID.
     *
     * @param userId the user ID
     * @return the user
     * @throws IllegalArgumentException if the user is not found
     */
    private User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }

    /**
     * Get a friendship by ID.
     *
     * @param friendshipId the friendship ID
     * @return the friendship
     * @throws IllegalArgumentException if the friendship is not found
     */
    private Friendship getFriendshipById(UUID friendshipId) {
        return friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new IllegalArgumentException("Friendship not found with ID: " + friendshipId));
    }
}
