package co.id.virtual.game.web.app.security;

import co.id.virtual.game.web.app.repository.BlacklistedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import jakarta.annotation.PostConstruct;

/**
 * Utility class for JWT token operations.
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secretString;

    private Key secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    @Autowired
    public JwtUtil(BlacklistedTokenRepository blacklistedTokenRepository) {
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    /**
     * Initialize the secret key once at startup.
     */
    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    /**
     * Generate a JWT token for a user.
     *
     * @param userDetails the user details
     * @return the JWT token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Generate a refresh token for a user.
     *
     * @param userDetails the user details
     * @return the refresh token
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("refresh", true);
        return createRefreshToken(claims, userDetails.getUsername());
    }

    /**
     * Validate a token.
     *
     * @param token the token to validate
     * @param userDetails the user details
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            // Check if token is blacklisted
            if (blacklistedTokenRepository.isTokenBlacklisted(token)) {
                logger.debug("Token is blacklisted: {}", token);
                return false;
            }

            // If userDetails is null, only check if token is expired
            if (userDetails == null) {
                boolean isExpired = isTokenExpired(token);
                if (isExpired) {
                    logger.debug("Token is expired");
                }
                return !isExpired;
            }

            final String username = getUsernameFromToken(token);
            boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

            if (!isValid) {
                logger.debug("Token validation failed for user: {}", username);
            }

            return isValid;
        } catch (ExpiredJwtException e) {
            logger.debug("JWT token is expired: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            logger.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            logger.warn("Unsupported JWT token: {}", e.getMessage());
            return false;
        } catch (SignatureException e) {
            logger.warn("JWT signature validation failed: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            logger.warn("JWT token compact of handler are invalid: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("JWT token validation error: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Blacklist a token.
     *
     * @param token the token to blacklist
     */
    public void blacklistToken(String token) {
        blacklistToken(token, null);
    }

    /**
     * Blacklist a token with associated user ID.
     *
     * @param token the token to blacklist
     * @param userId the ID of the user who owns the token
     */
    public void blacklistToken(String token, UUID userId) {
        try {
            Date expirationDate = getExpirationDateFromToken(token);
            if (userId != null) {
                blacklistedTokenRepository.blacklistToken(token, expirationDate.getTime(), userId);
            } else {
                blacklistedTokenRepository.blacklistToken(token, expirationDate.getTime());
            }
            logger.info("Token blacklisted successfully" + (userId != null ? " for user " + userId : ""));
        } catch (ExpiredJwtException e) {
            // Token is already expired, no need to blacklist
            logger.warn("Attempted to blacklist an already expired token: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Cannot blacklist malformed token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Cannot blacklist unsupported JWT token: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.error("Cannot blacklist JWT token with invalid signature: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Cannot blacklist token with empty claims: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error blacklisting token: {}", e.getMessage());
        }
    }

    /**
     * Blacklist all tokens for a user.
     *
     * @param userId the ID of the user whose tokens should be blacklisted
     * @return the number of tokens blacklisted
     */
    public int blacklistAllUserTokens(UUID userId) {
        try {
            int count = blacklistedTokenRepository.blacklistAllUserTokens(userId);
            logger.info("Blacklisted {} tokens for user {}", count, userId);
            return count;
        } catch (Exception e) {
            logger.error("Error blacklisting all tokens for user {}: {}", userId, e.getMessage());
            return 0;
        }
    }

    /**
     * Get all blacklisted tokens for a user.
     *
     * @param userId the ID of the user
     * @return a list of blacklisted tokens
     */
    public List<String> getBlacklistedTokensForUser(UUID userId) {
        try {
            List<String> tokens = blacklistedTokenRepository.getBlacklistedTokensForUser(userId);
            logger.debug("Found {} blacklisted tokens for user {}", tokens.size(), userId);
            return tokens;
        } catch (Exception e) {
            logger.error("Error getting blacklisted tokens for user {}: {}", userId, e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Get the username from a token.
     *
     * @param token the token
     * @return the username
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Get the expiration date from a token.
     *
     * @param token the token
     * @return the expiration date
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Get a claim from a token.
     *
     * @param token the token
     * @param claimsResolver the claims resolver function
     * @param <T> the type of the claim
     * @return the claim
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Get all claims from a token.
     *
     * @param token the token
     * @return the claims
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Check if a token is expired.
     *
     * @param token the token
     * @return true if the token is expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            logger.warn("Error checking if token is expired: {}", e.getMessage());
            // If we can't determine expiration, assume it's expired for safety
            return true;
        }
    }

    /**
     * Create a token.
     *
     * @param claims the claims
     * @param subject the subject
     * @return the token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Create a refresh token.
     *
     * @param claims the claims
     * @param subject the subject
     * @return the refresh token
     */
    private String createRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

}
