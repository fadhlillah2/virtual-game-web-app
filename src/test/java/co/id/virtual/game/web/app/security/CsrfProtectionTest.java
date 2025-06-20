package co.id.virtual.game.web.app.security;

import co.id.virtual.game.web.app.config.SecurityConfig;
import co.id.virtual.game.web.app.controller.AuthController;
import co.id.virtual.game.web.app.controller.GameController;
import co.id.virtual.game.web.app.filter.RateLimitingFilter;
import co.id.virtual.game.web.app.service.AuthService;
import co.id.virtual.game.web.app.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for CSRF protection.
 */
@WebMvcTest({GameController.class, AuthController.class})
public class CsrfProtectionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private AuthService authService;

    /**
     * Test that a POST request to a protected endpoint without a CSRF token is rejected.
     */
    @Test
    @WithMockUser
    public void testPostRequestWithoutCsrfTokenIsRejected() throws Exception {
        // Perform a POST request without a CSRF token
        mockMvc.perform(post("/api/game/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"gameType\": \"POKER\"}"))
                .andExpect(status().isForbidden());
    }

    /**
     * Test that a POST request to a protected endpoint with a valid CSRF token is accepted.
     */
    @Test
    @WithMockUser
    public void testPostRequestWithCsrfTokenIsAccepted() throws Exception {
        // Perform a POST request with a valid CSRF token
        mockMvc.perform(post("/api/game/start")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"gameType\": \"POKER\"}"))
                .andExpect(status().isOk());
    }

    /**
     * Test that a POST request to an endpoint that ignores CSRF protection is accepted without a token.
     */
    @Test
    public void testPostRequestToIgnoredEndpointIsAccepted() throws Exception {
        // Perform a POST request to an endpoint that ignores CSRF protection
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testuser\", \"password\": \"password\"}"))
                .andExpect(status().isOk());
    }
}
