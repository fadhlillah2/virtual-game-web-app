package co.id.virtual.game.web.app.service.impl;

import co.id.virtual.game.web.app.dto.chip.ChipBalanceDto;
import co.id.virtual.game.web.app.dto.chip.DailyBonusResponse;
import co.id.virtual.game.web.app.dto.chip.GiftResponse;
import co.id.virtual.game.web.app.dto.chip.TransactionDto;
import co.id.virtual.game.web.app.exception.DailyBonusAlreadyClaimedException;
import co.id.virtual.game.web.app.exception.DailyGiftLimitExceededException;
import co.id.virtual.game.web.app.exception.InsufficientChipsException;
import co.id.virtual.game.web.app.model.Transaction;
import co.id.virtual.game.web.app.model.TransactionType;
import co.id.virtual.game.web.app.model.User;
import co.id.virtual.game.web.app.repository.TransactionRepository;
import co.id.virtual.game.web.app.repository.UserRepository;
import co.id.virtual.game.web.app.service.ChipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of the ChipService interface.
 */
@Service
@Slf4j
public class ChipServiceImpl implements ChipService {
    
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    
    // Constants
    private static final Long DAILY_BONUS_AMOUNT = 500L;
    private static final Long DAILY_GIFT_LIMIT = 100L;
    
    @Autowired
    public ChipServiceImpl(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public ChipBalanceDto getUserBalance(UUID userId) {
        User user = findUserById(userId);
        
        // Get the last transaction time
        LocalDateTime lastTransactionTime = null;
        Transaction lastTransaction = transactionRepository.findByUserId(userId, Pageable.ofSize(1))
                .getContent()
                .stream()
                .findFirst()
                .orElse(null);
        
        if (lastTransaction != null) {
            lastTransactionTime = lastTransaction.getCreatedAt();
        }
        
        // Check if daily bonus has been claimed today
        boolean dailyBonusClaimed = hasDailyBonusBeenClaimedToday(userId);
        LocalDateTime lastDailyBonusTime = getLastDailyBonusTime(userId);
        
        return ChipBalanceDto.builder()
                .userId(userId)
                .chipsBalance(user.getChipsBalance())
                .totalChipsWon(user.getTotalChipsWon())
                .lastTransactionTime(lastTransactionTime)
                .dailyBonusClaimed(dailyBonusClaimed)
                .lastDailyBonusTime(lastDailyBonusTime)
                .build();
    }
    
    @Override
    @Transactional
    public DailyBonusResponse claimDailyBonus(UUID userId) throws DailyBonusAlreadyClaimedException {
        User user = findUserById(userId);
        
        // Check if daily bonus has already been claimed today
        if (hasDailyBonusBeenClaimedToday(userId)) {
            throw new DailyBonusAlreadyClaimedException();
        }
        
        // Calculate bonus amount and multiplier based on consecutive days
        int consecutiveDays = calculateConsecutiveDays(userId);
        double bonusMultiplier = calculateBonusMultiplier(consecutiveDays);
        long bonusAmount = Math.round(DAILY_BONUS_AMOUNT * bonusMultiplier);
        
        // Add chips to user's balance
        long balanceBefore = user.getChipsBalance();
        long balanceAfter = balanceBefore + bonusAmount;
        user.setChipsBalance(balanceAfter);
        userRepository.save(user);
        
        // Create transaction record
        Transaction transaction = Transaction.builder()
                .user(user)
                .type(TransactionType.DAILY_BONUS)
                .amount(bonusAmount)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .description("Daily bonus claimed: " + bonusAmount + " chips")
                .createdAt(LocalDateTime.now())
                .build();
        
        transactionRepository.save(transaction);
        
        // Calculate next bonus time (tomorrow)
        LocalDateTime nextBonusTime = LocalDate.now().plusDays(1).atStartOfDay();
        
        return DailyBonusResponse.builder()
                .userId(userId)
                .bonusAmount(bonusAmount)
                .newBalance(balanceAfter)
                .claimedAt(transaction.getCreatedAt())
                .nextBonusAvailableAt(nextBonusTime)
                .consecutiveDays(consecutiveDays + 1) // Increment for today's claim
                .bonusMultiplier(bonusMultiplier)
                .build();
    }
    
    @Override
    @Transactional
    public GiftResponse sendGift(UUID senderId, UUID recipientId, Long amount) 
            throws InsufficientChipsException, DailyGiftLimitExceededException {
        
        // Validate sender and recipient
        User sender = findUserById(senderId);
        User recipient = findUserById(recipientId);
        
        // Check if sender has enough chips
        if (!hasEnoughChips(senderId, amount)) {
            throw new InsufficientChipsException();
        }
        
        // Check if sender has exceeded daily gift limit
        Long giftedToday = getGiftedAmountToday(senderId);
        Long remainingLimit = DAILY_GIFT_LIMIT - giftedToday;
        
        if (amount > remainingLimit) {
            throw new DailyGiftLimitExceededException("Daily gift limit exceeded. Remaining limit: " + remainingLimit);
        }
        
        // Subtract chips from sender
        long senderBalanceBefore = sender.getChipsBalance();
        long senderBalanceAfter = senderBalanceBefore - amount;
        sender.setChipsBalance(senderBalanceAfter);
        userRepository.save(sender);
        
        // Add chips to recipient
        long recipientBalanceBefore = recipient.getChipsBalance();
        long recipientBalanceAfter = recipientBalanceBefore + amount;
        recipient.setChipsBalance(recipientBalanceAfter);
        userRepository.save(recipient);
        
        // Create transaction record for sender
        Transaction senderTransaction = Transaction.builder()
                .user(sender)
                .type(TransactionType.GIFT_SENT)
                .amount(-amount) // Negative amount for sender
                .balanceBefore(senderBalanceBefore)
                .balanceAfter(senderBalanceAfter)
                .referenceId(recipientId)
                .description("Gift sent to " + recipient.getUsername() + ": " + amount + " chips")
                .createdAt(LocalDateTime.now())
                .build();
        
        transactionRepository.save(senderTransaction);
        
        // Create transaction record for recipient
        Transaction recipientTransaction = Transaction.builder()
                .user(recipient)
                .type(TransactionType.GIFT_RECEIVED)
                .amount(amount)
                .balanceBefore(recipientBalanceBefore)
                .balanceAfter(recipientBalanceAfter)
                .referenceId(senderId)
                .description("Gift received from " + sender.getUsername() + ": " + amount + " chips")
                .createdAt(LocalDateTime.now())
                .build();
        
        transactionRepository.save(recipientTransaction);
        
        // Calculate remaining gift limit
        Long remainingGiftLimit = DAILY_GIFT_LIMIT - (giftedToday + amount);
        
        return GiftResponse.builder()
                .transactionId(senderTransaction.getId())
                .senderId(senderId)
                .senderUsername(sender.getUsername())
                .recipientId(recipientId)
                .recipientUsername(recipient.getUsername())
                .amount(amount)
                .senderBalanceAfter(senderBalanceAfter)
                .recipientBalanceAfter(recipientBalanceAfter)
                .sentAt(senderTransaction.getCreatedAt())
                .remainingDailyGiftLimit(remainingGiftLimit)
                .build();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDto> getTransactionHistory(UUID userId, TransactionType type, Pageable pageable) {
        // Find user to ensure it exists
        User user = findUserById(userId);
        
        // Get transactions
        Page<Transaction> transactions;
        if (type != null) {
            transactions = transactionRepository.findByUserIdAndType(userId, type, pageable);
        } else {
            transactions = transactionRepository.findByUserId(userId, pageable);
        }
        
        // Convert to DTOs
        return transactions.map(this::convertToTransactionDto);
    }
    
    @Override
    @Transactional
    public ChipBalanceDto addChips(UUID userId, Long amount, TransactionType type, String description, UUID referenceId) {
        User user = findUserById(userId);
        
        long balanceBefore = user.getChipsBalance();
        long balanceAfter = balanceBefore + amount;
        
        user.setChipsBalance(balanceAfter);
        userRepository.save(user);
        
        // Create transaction record
        Transaction transaction = Transaction.builder()
                .user(user)
                .type(type)
                .amount(amount)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .referenceId(referenceId)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();
        
        transactionRepository.save(transaction);
        
        return getUserBalance(userId);
    }
    
    @Override
    @Transactional
    public ChipBalanceDto subtractChips(UUID userId, Long amount, TransactionType type, String description, UUID referenceId) 
            throws InsufficientChipsException {
        
        User user = findUserById(userId);
        
        // Check if user has enough chips
        if (!hasEnoughChips(userId, amount)) {
            throw new InsufficientChipsException();
        }
        
        long balanceBefore = user.getChipsBalance();
        long balanceAfter = balanceBefore - amount;
        
        user.setChipsBalance(balanceAfter);
        userRepository.save(user);
        
        // Create transaction record
        Transaction transaction = Transaction.builder()
                .user(user)
                .type(type)
                .amount(-amount) // Negative amount for subtraction
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .referenceId(referenceId)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();
        
        transactionRepository.save(transaction);
        
        return getUserBalance(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasEnoughChips(UUID userId, Long amount) {
        User user = findUserById(userId);
        return user.getChipsBalance() >= amount;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getDailyGiftLimit(UUID userId) {
        // Ensure user exists
        findUserById(userId);
        return DAILY_GIFT_LIMIT;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getRemainingDailyGiftLimit(UUID userId) {
        // Ensure user exists
        findUserById(userId);
        
        Long giftedToday = getGiftedAmountToday(userId);
        return DAILY_GIFT_LIMIT - giftedToday;
    }
    
    // Helper methods
    
    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }
    
    private boolean hasDailyBonusBeenClaimedToday(UUID userId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        
        return transactionRepository.findByUserIdAndTypeAndCreatedAtBetween(
                userId, TransactionType.DAILY_BONUS, startOfDay, endOfDay, Pageable.ofSize(1))
                .hasContent();
    }
    
    private LocalDateTime getLastDailyBonusTime(UUID userId) {
        Transaction lastBonus = transactionRepository.findByUserIdAndType(
                userId, TransactionType.DAILY_BONUS, Pageable.ofSize(1))
                .getContent()
                .stream()
                .findFirst()
                .orElse(null);
        
        return lastBonus != null ? lastBonus.getCreatedAt() : null;
    }
    
    private int calculateConsecutiveDays(UUID userId) {
        // This is a simplified implementation
        // In a real application, you would need to check for consecutive days
        return 0;
    }
    
    private double calculateBonusMultiplier(int consecutiveDays) {
        // Simple multiplier based on consecutive days
        if (consecutiveDays >= 30) return 2.0;
        if (consecutiveDays >= 15) return 1.5;
        if (consecutiveDays >= 7) return 1.25;
        return 1.0;
    }
    
    private Long getGiftedAmountToday(UUID userId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        
        Long amount = transactionRepository.sumAmountByUserIdAndTypeAndTimeRange(
                userId, TransactionType.GIFT_SENT, startOfDay, endOfDay);
        
        return amount != null ? Math.abs(amount) : 0L;
    }
    
    private TransactionDto convertToTransactionDto(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .userId(transaction.getUser().getId())
                .username(transaction.getUser().getUsername())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .balanceBefore(transaction.getBalanceBefore())
                .balanceAfter(transaction.getBalanceAfter())
                .referenceId(transaction.getReferenceId())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
