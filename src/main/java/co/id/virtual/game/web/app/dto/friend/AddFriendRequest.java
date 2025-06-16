package co.id.virtual.game.web.app.dto.friend;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for adding a friend.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddFriendRequest {
    
    /**
     * The username of the user to add as a friend.
     */
    @NotBlank(message = "Username is required")
    private String username;
}
