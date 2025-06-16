package co.id.virtual.game.web.app.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Interceptor for WebSocket messages to handle user authentication.
 */
@Slf4j
@Component
public class UserInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorization = getAuthorizationHeader(accessor);

            if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
                String token = authorization.substring(7);

                try {
                    // Extract user information from token and validate
                    String username = jwtUtil.getUsernameFromToken(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtUtil.validateToken(token, userDetails)) {
                        // Create authentication token
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                        // Set user information in message headers
                        accessor.setUser(auth);
                        log.debug("WebSocket connection authenticated for user: {}", username);
                    }
                } catch (Exception e) {
                    log.error("Error authenticating WebSocket connection", e);
                }
            }
        }

        return message;
    }

    private String getAuthorizationHeader(StompHeaderAccessor accessor) {
        // Try to get from native headers
        Map<String, Object> nativeHeaders = accessor.getMessageHeaders()
                .get(SimpMessageHeaderAccessor.NATIVE_HEADERS, Map.class);

        if (nativeHeaders != null) {
            // Check for Authorization header
            Object authHeader = nativeHeaders.get("Authorization");

            if (authHeader instanceof List && !((List<?>) authHeader).isEmpty()) {
                return ((List<?>) authHeader).get(0).toString();
            }
        }

        return null;
    }
}
