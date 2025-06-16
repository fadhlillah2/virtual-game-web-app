package co.id.virtual.game.web.app.exception;

/**
 * Exception thrown when a user attempts to send gifts that exceed the daily gift limit.
 */
public class DailyGiftLimitExceededException extends RuntimeException {
    
    public DailyGiftLimitExceededException() {
        super("Daily gift limit has been exceeded");
    }
    
    public DailyGiftLimitExceededException(String message) {
        super(message);
    }
    
    public DailyGiftLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
