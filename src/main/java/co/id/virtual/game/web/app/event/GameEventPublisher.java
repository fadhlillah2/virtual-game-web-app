package co.id.virtual.game.web.app.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service for publishing game events to Kafka topics.
 */
@Service
@Slf4j
public class GameEventPublisher {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    private static final String GAME_EVENTS_TOPIC = "game-events";
    private static final String USER_ACTIONS_TOPIC = "user-actions";
    private static final String CHIP_TRANSACTIONS_TOPIC = "chip-transactions";
    
    @Autowired
    public GameEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    /**
     * Publish a game started event.
     *
     * @param event the game started event
     */
    public void publishGameStarted(GameStartedEvent event) {
        log.info("Publishing game started event: {}", event.getGameId());
        kafkaTemplate.send(GAME_EVENTS_TOPIC, "game-started", event);
    }
    
    /**
     * Publish a game ended event.
     *
     * @param event the game ended event
     */
    public void publishGameEnded(GameEndedEvent event) {
        log.info("Publishing game ended event: {}", event.getGameId());
        kafkaTemplate.send(GAME_EVENTS_TOPIC, "game-ended", event);
    }
    
    /**
     * Publish a user action event.
     *
     * @param event the user action event
     */
    public void publishUserAction(UserActionEvent event) {
        log.info("Publishing user action: {} in game {}", 
                event.getAction(), event.getGameId());
        kafkaTemplate.send(USER_ACTIONS_TOPIC, event.getUserId().toString(), event);
    }
    
    /**
     * Publish a chip transaction event.
     *
     * @param event the chip transaction event
     */
    public void publishChipTransaction(ChipTransactionEvent event) {
        log.info("Publishing chip transaction: {} chips for user {}", 
                event.getAmount(), event.getUserId());
        kafkaTemplate.send(CHIP_TRANSACTIONS_TOPIC, event.getUserId().toString(), event);
    }
}
