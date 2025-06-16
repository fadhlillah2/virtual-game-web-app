package co.id.virtual.game.web.app.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Listener for game events from Kafka topics.
 */
@Component
@Slf4j
public class GameEventListener {

    /**
     * Listen for game events.
     *
     * @param event the event
     * @param topic the topic
     * @param key the message key
     */
    @KafkaListener(topics = "game-events", groupId = "analytics-group")
    public void handleGameEvents(
            @Payload Object event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_KEY) String key) {

        log.info("Received game event: {} - {}", key, event);

        if ("game-started".equals(key) && event instanceof GameStartedEvent) {
            processGameStarted((GameStartedEvent) event);
        } else if ("game-ended".equals(key) && event instanceof GameEndedEvent) {
            processGameEnded((GameEndedEvent) event);
        }
    }

    /**
     * Listen for user actions.
     *
     * @param event the event
     * @param partition the partition
     */
    @KafkaListener(topics = "user-actions", groupId = "monitoring-group")
    public void handleUserActions(
            @Payload UserActionEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {

        log.info("Received user action: {} from user {} in partition {}", 
                event.getAction(), event.getUserId(), partition);

        processUserAction(event);
    }

    /**
     * Listen for chip transactions.
     *
     * @param event the event
     */
    @KafkaListener(topics = "chip-transactions", groupId = "transaction-group")
    public void handleChipTransactions(@Payload ChipTransactionEvent event) {
        log.info("Processing chip transaction: {} chips for user {}", 
                event.getAmount(), event.getUserId());

        processChipTransaction(event);
    }

    /**
     * Process a game started event.
     *
     * @param event the event
     */
    private void processGameStarted(GameStartedEvent event) {
        // In a real implementation, this would:
        // 1. Record analytics data
        // 2. Update active games count
        // 3. Send notifications to relevant users
        log.debug("Processing game started: {}", event.getSessionId());
    }

    /**
     * Process a game ended event.
     *
     * @param event the event
     */
    private void processGameEnded(GameEndedEvent event) {
        // In a real implementation, this would:
        // 1. Record analytics data
        // 2. Update leaderboards
        // 3. Send notifications to relevant users
        log.debug("Processing game ended: {}", event.getSessionId());
    }

    /**
     * Process a user action event.
     *
     * @param event the event
     */
    private void processUserAction(UserActionEvent event) {
        // In a real implementation, this would:
        // 1. Record analytics data
        // 2. Check for suspicious activity
        // 3. Update user activity metrics
        log.debug("Processing user action: {} by {}", event.getAction(), event.getUserId());
    }

    /**
     * Process a chip transaction event.
     *
     * @param event the event
     */
    private void processChipTransaction(ChipTransactionEvent event) {
        // In a real implementation, this would:
        // 1. Record analytics data
        // 2. Check for suspicious transactions
        // 3. Update user balance metrics
        log.debug("Processing chip transaction: {} chips for {}", event.getAmount(), event.getUserId());
    }
}
