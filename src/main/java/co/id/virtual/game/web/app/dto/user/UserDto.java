package co.id.virtual.game.web.app.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for User entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    
    private UUID id;
    private String username;
    private String email;
    private LocalDate birthDate;
    private String avatarUrl;
    private Long chipsBalance;
    private Long totalChipsWon;
    private Integer level;
    private Integer experiencePoints;
    private Boolean isPremium;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
}
