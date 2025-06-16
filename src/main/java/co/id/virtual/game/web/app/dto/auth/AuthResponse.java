package co.id.virtual.game.web.app.dto.auth;

import co.id.virtual.game.web.app.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private UserDto user;

    @Builder.Default
    private String tokenType = "Bearer";
}
