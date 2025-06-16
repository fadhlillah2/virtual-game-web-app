package co.id.virtual.game.web.app.exception;

/**
 * Exception thrown when a user attempts to perform an action that requires more chips than they currently have.
 */
public class InsufficientChipsException extends RuntimeException {
    
    public InsufficientChipsException() {
        super("Insufficient chips to perform this action");
    }
    
    public InsufficientChipsException(String message) {
        super(message);
    }
    
    public InsufficientChipsException(String message, Throwable cause) {
        super(message, cause);
    }
}
