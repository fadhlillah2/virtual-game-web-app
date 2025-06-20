package co.id.virtual.game.web.app.exception;

import co.id.virtual.game.web.app.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for the application.
 * Handles various exceptions and returns appropriate responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles CSRF token exceptions.
     *
     * @param ex the exception
     * @return a response entity with an error message
     */
    @ExceptionHandler({InvalidCsrfTokenException.class, MissingCsrfTokenException.class})
    public ResponseEntity<ApiResponse<Void>> handleCsrfException(Exception ex) {
        String message = "CSRF token validation failed. Please refresh the page and try again.";
        ApiResponse<Void> response = ApiResponse.error(message);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles general security exceptions.
     *
     * @param ex the exception
     * @return a response entity with an error message
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(Exception ex) {
        String message = "Access denied. You don't have permission to perform this action.";
        ApiResponse<Void> response = ApiResponse.error(message);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Fallback handler for all other exceptions.
     *
     * @param ex the exception
     * @return a response entity with an error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllExceptions(Exception ex) {
        String message = "An unexpected error occurred. Please try again later.";
        ApiResponse<Void> response = ApiResponse.error(message);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
