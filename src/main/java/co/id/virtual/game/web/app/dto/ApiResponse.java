package co.id.virtual.game.web.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API response wrapper for all controller responses.
 * @param <T> The type of data contained in the response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    
    /**
     * Create a success response with data and message.
     *
     * @param message The success message
     * @param data The response data
     * @param <T> The type of data
     * @return A success response
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }
    
    /**
     * Create a success response with only a message.
     *
     * @param message The success message
     * @param <T> The type of data
     * @return A success response with null data
     */
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, null);
    }
    
    /**
     * Create an error response with a message.
     *
     * @param message The error message
     * @param <T> The type of data
     * @return An error response with null data
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
    
    /**
     * Create an error response with a message and data.
     *
     * @param message The error message
     * @param data The response data
     * @param <T> The type of data
     * @return An error response
     */
    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(false, message, data);
    }
}
