package co.id.virtual.game.web.app.repository;

import java.util.UUID;
import java.util.List;

/**
 * Repository interface for managing blacklisted tokens.
 */
public interface BlacklistedTokenRepository {

    /**
     * Add a token to the blacklist.
     *
     * @param token the token to blacklist
     * @param expirationTime the expiration time of the token in milliseconds
     */
    void blacklistToken(String token, long expirationTime);

    /**
     * Add a token to the blacklist with associated user ID.
     *
     * @param token the token to blacklist
     * @param expirationTime the expiration time of the token in milliseconds
     * @param userId the ID of the user who owns the token
     */
    void blacklistToken(String token, long expirationTime, UUID userId);

    /**
     * Check if a token is blacklisted.
     *
     * @param token the token to check
     * @return true if the token is blacklisted, false otherwise
     */
    boolean isTokenBlacklisted(String token);

    /**
     * Remove a token from the blacklist.
     * This is mainly for testing purposes.
     *
     * @param token the token to remove
     */
    void removeFromBlacklist(String token);

    /**
     * Blacklist all tokens for a specific user.
     * This will find all tokens associated with the user ID and blacklist them.
     *
     * @param userId the ID of the user whose tokens should be blacklisted
     * @return the number of tokens blacklisted
     */
    int blacklistAllUserTokens(UUID userId);

    /**
     * Get all blacklisted tokens for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of blacklisted tokens
     */
    List<String> getBlacklistedTokensForUser(UUID userId);
}
