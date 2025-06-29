package co.id.virtual.game.web.app.security;

import co.id.virtual.game.web.app.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityHeadersTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    

    /**
     * Test that security headers are included in HTTP responses.
     */
    @Test
    @WithMockUser
    public void testSecurityHeadersAreIncluded() throws Exception {
        // Perform a GET request to a protected endpoint
        mockMvc.perform(get("/api/game/status"))
                .andExpect(status().isOk())
                // Test X-Content-Type-Options header
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                // Test X-Frame-Options header
                .andExpect(header().string("X-Frame-Options", "DENY"))
                // Test Strict-Transport-Security header
                .andExpect(header().string("Strict-Transport-Security", "max-age=31536000 ; includeSubDomains"))
                // Test Content-Security-Policy header
                .andExpect(header().string("Content-Security-Policy", 
                        "default-src 'self'; script-src 'self'; img-src 'self'; style-src 'self'; connect-src 'self'; frame-ancestors 'none'; form-action 'self'"))
                // Test X-XSS-Protection header
                .andExpect(header().string("X-XSS-Protection", "1; mode=block"))
                // Test Referrer-Policy header
                .andExpect(header().string("Referrer-Policy", "same-origin"));
    }
}
