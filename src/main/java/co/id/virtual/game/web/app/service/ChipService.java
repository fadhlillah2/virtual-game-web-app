package co.id.virtual.game.web.app.service;

import co.id.virtual.game.web.app.dto.chip.ChipBalanceDto;
import co.id.virtual.game.web.app.dto.chip.DailyBonusResponse;
import co.id.virtual.game.web.app.dto.chip.GiftResponse;
import co.id.virtual.game.web.app.dto.chip.TransactionDto;
import co.id.virtual.game.web.app.exception.DailyBonusAlreadyClaimedException;
import co.id.virtual.game.web.app.exception.DailyGiftLimitExceededException;
import co.id.virtual.game.web.app.exception.InsufficientChipsException;
import co.id.virtual.game.web.app.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service interface for managing chip operations.
 */
public interface ChipService {
    
    /**
     * Get a user's chip balance.
     *
     * @param userId the ID of the user
     * @return the user's chip balance
     */
    ChipBalanceDto getUserBalance(UUID userId);
    
    /**
     * Claim the daily bonus for a user.
     *
     * @param userId the ID of the user
     * @return the daily bonus response
     * @throws DailyBonusAlreadyClaimedException if the user has already claimed their daily bonus today
     */
    DailyBonusResponse claimDailyBonus(UUID userId) throws DailyBonusAlreadyClaimedException;
    
    /**
     * Send a gift to another user.
     *
     * @param senderId the ID of the sender
     * @param recipientId the ID of the recipient
     * @param amount the amount of chips to send
     * @return the gift response
     * @throws InsufficientChipsException if the sender does not have enough chips
     * @throws DailyGiftLimitExceededException if the sender has exceeded their daily gift limit
     */
    GiftResponse sendGift(UUID senderId, UUID recipientId, Long amount) 
            throws InsufficientChipsException, DailyGiftLimitExceededException;
    
    /**
     * Get a user's transaction history.
     *
     * @param userId the ID of the user
     * @param type the transaction type filter (optional)
     * @param pageable pagination information
     * @return a page of the user's transaction history
     */
    Page<TransactionDto> getTransactionHistory(UUID userId, TransactionType type, Pageable pageable);
    
    /**
     * Add chips to a user's balance.
     *
     * @param userId the ID of the user
     * @param amount the amount of chips to add
     * @param type the transaction type
     * @param description the transaction description
     * @param referenceId the reference ID (optional)
     * @return the updated chip balance
     */
    ChipBalanceDto addChips(UUID userId, Long amount, TransactionType type, String description, UUID referenceId);
    
    /**
     * Subtract chips from a user's balance.
     *
     * @param userId the ID of the user
     * @param amount the amount of chips to subtract
     * @param type the transaction type
     * @param description the transaction description
     * @param referenceId the reference ID (optional)
     * @return the updated chip balance
     * @throws InsufficientChipsException if the user does not have enough chips
     */
    ChipBalanceDto subtractChips(UUID userId, Long amount, TransactionType type, String description, UUID referenceId) 
            throws InsufficientChipsException;
    
    /**
     * Check if a user has enough chips.
     *
     * @param userId the ID of the user
     * @param amount the amount of chips to check
     * @return true if the user has enough chips, false otherwise
     */
    boolean hasEnoughChips(UUID userId, Long amount);
    
    /**
     * Get the daily gift limit for a user.
     *
     * @param userId the ID of the user
     * @return the daily gift limit
     */
    Long getDailyGiftLimit(UUID userId);
    
    /**
     * Get the remaining daily gift limit for a user.
     *
     * @param userId the ID of the user
     * @return the remaining daily gift limit
     */
    Long getRemainingDailyGiftLimit(UUID userId);
}
