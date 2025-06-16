package co.id.virtual.game.web.app.dto.friend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for friend information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendDto {
    
    /**
     * The friend's user ID.
     */
    private String id;
    
    /**
     * The friend's username.
     */
    private String username;
    
    /**
     * The friend's avatar URL.
     */
    private String avatarUrl;
    
    /**
     * Whether the friend is online.
     */
    private boolean online;
    
    /**
     * The friend's last activity time.
     */
    private LocalDateTime lastActivity;
    
    /**
     * The friend's current chips balance.
     */
    private long chipsBalance;
    
    /**
     * The friendship status (PENDING, ACCEPTED).
     */
    private String status;
    
    /**
     * Whether the current user is the requester of the friendship.
     */
    private boolean isRequester;
    
    /**
     * When the friendship was created.
     */
    private LocalDateTime createdAt;
}
