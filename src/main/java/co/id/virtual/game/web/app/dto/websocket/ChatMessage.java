package co.id.virtual.game.web.app.dto.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for representing a chat message.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    
    /**
     * The ID of the room where the message was sent.
     */
    private String roomId;
    
    /**
     * The ID of the sender.
     */
    private UUID senderId;
    
    /**
     * The username of the sender.
     */
    private String sender;
    
    /**
     * The content of the message.
     */
    private String content;
    
    /**
     * The timestamp when the message was sent.
     */
    private LocalDateTime timestamp;
    
    /**
     * The type of message (e.g., "text", "system", "private").
     */
    private String type;
    
    /**
     * The ID of the recipient (for private messages).
     */
    private UUID recipientId;
    
    /**
     * The username of the recipient (for private messages).
     */
    private String recipient;
}
