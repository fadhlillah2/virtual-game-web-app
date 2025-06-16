package co.id.virtual.game.web.app.exception;

/**
 * Exception thrown when a user attempts to claim a daily bonus that has already been claimed.
 */
public class DailyBonusAlreadyClaimedException extends RuntimeException {
    
    public DailyBonusAlreadyClaimedException() {
        super("Daily bonus has already been claimed today");
    }
    
    public DailyBonusAlreadyClaimedException(String message) {
        super(message);
    }
    
    public DailyBonusAlreadyClaimedException(String message, Throwable cause) {
        super(message, cause);
    }
}
