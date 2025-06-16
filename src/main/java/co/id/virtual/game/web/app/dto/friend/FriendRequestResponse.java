package co.id.virtual.game.web.app.dto.friend;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for responding to a friend request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestResponse {
    
    /**
     * The ID of the friendship.
     */
    @NotBlank(message = "Friendship ID is required")
    private String friendshipId;
    
    /**
     * Whether to accept the friend request.
     */
    @NotNull(message = "Accept status is required")
    private Boolean accept;
}
