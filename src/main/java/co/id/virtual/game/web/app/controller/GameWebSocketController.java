package co.id.virtual.game.web.app.controller;

import co.id.virtual.game.web.app.dto.websocket.ChatMessage;
import co.id.virtual.game.web.app.dto.websocket.GameActionMessage;
import co.id.virtual.game.web.app.dto.websocket.GameActionResponse;
import co.id.virtual.game.web.app.dto.websocket.GameStateMessage;
import co.id.virtual.game.web.app.model.GameState;
import co.id.virtual.game.web.app.security.UserPrincipal;
import co.id.virtual.game.web.app.service.GameService;
import co.id.virtual.game.web.app.service.GameSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Controller for handling WebSocket messages related to games.
 */
@Controller
@Slf4j
public class GameWebSocketController {
    
    private final GameService gameService;
    private final GameSessionService gameSessionService;
    private final SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    public GameWebSocketController(
            GameService gameService,
            GameSessionService gameSessionService,
            SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.gameSessionService = gameSessionService;
        this.messagingTemplate = messagingTemplate;
    }
    
    /**
     * Handle a request to join a game.
     *
     * @param gameId the ID of the game to join
     * @param message the join game message
     * @param principal the authenticated user
     * @return the updated game state
     */
    @MessageMapping("/game.join/{gameId}")
    @SendTo("/topic/game/{gameId}")
    public GameStateMessage joinGame(
            @DestinationVariable String gameId,
            @Payload GameActionMessage message,
            Principal principal) {
        
        log.info("User {} joining game {}", principal.getName(), gameId);
        
        UserPrincipal userPrincipal = getUserPrincipal(principal);
        
        // Process the join request
        GameState gameState = gameSessionService.joinGameSession(
            UUID.fromString(gameId),
            userPrincipal.getId(),
            message.getAmount()
        );
        
        // Convert to DTO
        return convertToGameStateMessage(gameState);
    }
    
    /**
     * Handle a game action.
     *
     * @param gameId the ID of the game
     * @param action the game action
     * @param principal the authenticated user
     * @return the response to the action
     */
    @MessageMapping("/game.action/{gameId}")
    public void handleGameAction(
            @DestinationVariable String gameId,
            @Payload GameActionMessage action,
            Principal principal) {
        
        log.info("Game action from {}: {} in game {}", 
                principal.getName(), action.getAction(), gameId);
        
        UserPrincipal userPrincipal = getUserPrincipal(principal);
        
        // Process the game action
        GameActionResponse response = gameSessionService.processGameAction(
            UUID.fromString(gameId),
            userPrincipal.getId(),
            action
        );
        
        // Send the response to all players in the game
        messagingTemplate.convertAndSend(
            "/topic/game/" + gameId + "/action", 
            response
        );
        
        // Update the game state
        GameState gameState = gameSessionService.getGameState(UUID.fromString(gameId));
        GameStateMessage stateMessage = convertToGameStateMessage(gameState);
        
        // Send the updated game state to all players in the game
        messagingTemplate.convertAndSend(
            "/topic/game/" + gameId, 
            stateMessage
        );
    }
    
    /**
     * Handle a chat message.
     *
     * @param roomId the ID of the chat room
     * @param message the chat message
     * @param principal the authenticated user
     * @return the processed chat message
     */
    @MessageMapping("/chat.message/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessage handleChatMessage(
            @DestinationVariable String roomId,
            @Payload ChatMessage message,
            Principal principal) {
        
        UserPrincipal userPrincipal = getUserPrincipal(principal);
        
        // Set sender information
        message.setSenderId(userPrincipal.getId());
        message.setSender(userPrincipal.getUsername());
        message.setTimestamp(LocalDateTime.now());
        
        // Log chat message
        log.info("Chat message in room {}: {} - {}", 
                roomId, userPrincipal.getUsername(), message.getContent());
        
        return message;
    }
    
    /**
     * Send a private chat message.
     *
     * @param message the chat message
     * @param principal the authenticated user
     */
    @MessageMapping("/chat.private")
    public void handlePrivateChatMessage(
            @Payload ChatMessage message,
            Principal principal) {
        
        UserPrincipal userPrincipal = getUserPrincipal(principal);
        
        // Set sender information
        message.setSenderId(userPrincipal.getId());
        message.setSender(userPrincipal.getUsername());
        message.setTimestamp(LocalDateTime.now());
        message.setType("private");
        
        // Log private chat message
        log.info("Private chat message from {} to {}: {}", 
                userPrincipal.getUsername(), message.getRecipient(), message.getContent());
        
        // Send to recipient
        messagingTemplate.convertAndSendToUser(
            message.getRecipient(),
            "/queue/private.messages",
            message
        );
        
        // Send a copy to sender
        messagingTemplate.convertAndSendToUser(
            userPrincipal.getUsername(),
            "/queue/private.messages",
            message
        );
    }
    
    /**
     * Get the UserPrincipal from the Principal.
     *
     * @param principal the Principal
     * @return the UserPrincipal
     */
    private UserPrincipal getUserPrincipal(Principal principal) {
        if (principal instanceof Authentication) {
            Object details = ((Authentication) principal).getPrincipal();
            if (details instanceof UserPrincipal) {
                return (UserPrincipal) details;
            }
        }
        throw new IllegalStateException("Principal is not a UserPrincipal");
    }
    
    /**
     * Convert a GameState to a GameStateMessage.
     *
     * @param gameState the GameState
     * @return the GameStateMessage
     */
    private GameStateMessage convertToGameStateMessage(GameState gameState) {
        return GameStateMessage.builder()
                .gameId(gameState.getGameId())
                .sessionId(gameState.getSessionId())
                .gameName(gameState.getGameName())
                .gameType(gameState.getGameType())
                .gameState(gameState.getState())
                .roundNumber(gameState.getRoundNumber())
                .currentTurn(gameState.getCurrentTurn())
                .players(gameState.getPlayers())
                .playerNames(gameState.getPlayerNames())
                .playerChips(gameState.getPlayerChips())
                .playerStatus(gameState.getPlayerStatus())
                .lastActionTime(gameState.getLastActionTime())
                .potSize(gameState.getPotSize())
                .gameData(gameState.getGameData())
                .actionHistory(gameState.getActionHistory())
                .timestamp(LocalDateTime.now())
                .active(gameState.isActive())
                .minBet(gameState.getMinBet())
                .maxBet(gameState.getMaxBet())
                .build();
    }
}
