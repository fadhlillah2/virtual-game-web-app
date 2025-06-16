package co.id.virtual.game.web.app.controller;

import co.id.virtual.game.web.app.dto.ApiResponse;
import co.id.virtual.game.web.app.dto.friend.AddFriendRequest;
import co.id.virtual.game.web.app.dto.friend.FriendDto;
import co.id.virtual.game.web.app.dto.friend.FriendRequestResponse;
import co.id.virtual.game.web.app.model.Friendship;
import co.id.virtual.game.web.app.security.UserPrincipal;
import co.id.virtual.game.web.app.service.FriendService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

/**
 * Controller for handling friend-related operations.
 */
@Controller
@RequestMapping("/friends")
@PreAuthorize("hasRole('USER')")
@Slf4j
public class FriendController {
    
    private final FriendService friendService;
    
    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }
    
    /**
     * Display the friends page.
     *
     * @return the friends view
     */
    @GetMapping
    public ModelAndView showFriendsPage() {
        return new ModelAndView("friends");
    }
    
    /**
     * Get the current user's friends.
     *
     * @param userPrincipal the authenticated user
     * @return the list of friends
     */
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<FriendDto>>> getFriends(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<FriendDto> friends = friendService.getFriends(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Friends retrieved successfully", friends));
    }
    
    /**
     * Get the current user's pending friend requests.
     *
     * @param userPrincipal the authenticated user
     * @return the list of pending friend requests
     */
    @GetMapping("/api/pending")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<FriendDto>>> getPendingRequests(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<FriendDto> pendingRequests = friendService.getPendingRequests(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Pending requests retrieved successfully", pendingRequests));
    }
    
    /**
     * Send a friend request.
     *
     * @param userPrincipal the authenticated user
     * @param request the add friend request
     * @return a success response
     */
    @PostMapping("/api/add")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> addFriend(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody AddFriendRequest request) {
        
        try {
            friendService.sendFriendRequest(userPrincipal.getId(), request.getUsername());
            return ResponseEntity.ok(ApiResponse.success("Friend request sent successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Respond to a friend request.
     *
     * @param userPrincipal the authenticated user
     * @param response the friend request response
     * @return a success response
     */
    @PostMapping("/api/respond")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> respondToFriendRequest(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody FriendRequestResponse response) {
        
        try {
            UUID friendshipId = UUID.fromString(response.getFriendshipId());
            
            if (response.getAccept()) {
                friendService.acceptFriendRequest(userPrincipal.getId(), friendshipId);
                return ResponseEntity.ok(ApiResponse.success("Friend request accepted"));
            } else {
                friendService.rejectFriendRequest(userPrincipal.getId(), friendshipId);
                return ResponseEntity.ok(ApiResponse.success("Friend request rejected"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Remove a friend.
     *
     * @param userPrincipal the authenticated user
     * @param friendId the friend ID
     * @return a success response
     */
    @DeleteMapping("/api/{friendId}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> removeFriend(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable String friendId) {
        
        try {
            UUID friendUuid = UUID.fromString(friendId);
            friendService.removeFriend(userPrincipal.getId(), friendUuid);
            return ResponseEntity.ok(ApiResponse.success("Friend removed successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Block a user.
     *
     * @param userPrincipal the authenticated user
     * @param userId the ID of the user to block
     * @return a success response
     */
    @PostMapping("/api/block/{userId}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> blockUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable String userId) {
        
        try {
            UUID userUuid = UUID.fromString(userId);
            friendService.blockUser(userPrincipal.getId(), userUuid);
            return ResponseEntity.ok(ApiResponse.success("User blocked successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Unblock a user.
     *
     * @param userPrincipal the authenticated user
     * @param userId the ID of the user to unblock
     * @return a success response
     */
    @PostMapping("/api/unblock/{userId}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> unblockUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable String userId) {
        
        try {
            UUID userUuid = UUID.fromString(userId);
            friendService.unblockUser(userPrincipal.getId(), userUuid);
            return ResponseEntity.ok(ApiResponse.success("User unblocked successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Check if two users are friends.
     *
     * @param userPrincipal the authenticated user
     * @param userId the ID of the other user
     * @return whether the users are friends
     */
    @GetMapping("/api/check/{userId}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Boolean>> checkFriendship(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable String userId) {
        
        try {
            UUID userUuid = UUID.fromString(userId);
            boolean areFriends = friendService.areFriends(userPrincipal.getId(), userUuid);
            return ResponseEntity.ok(ApiResponse.success("Friendship status checked", areFriends));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
