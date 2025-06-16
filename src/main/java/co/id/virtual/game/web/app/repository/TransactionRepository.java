package co.id.virtual.game.web.app.repository;

import co.id.virtual.game.web.app.model.Transaction;
import co.id.virtual.game.web.app.model.TransactionType;
import co.id.virtual.game.web.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Transaction entity.
 * Provides methods to access and manipulate transaction data.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    /**
     * Find all transactions for a specific user.
     *
     * @param user the user to search for
     * @param pageable pagination information
     * @return a page of transactions for the specified user
     */
    Page<Transaction> findByUser(User user, Pageable pageable);

    /**
     * Find all transactions for a specific user and transaction type.
     *
     * @param user the user to search for
     * @param type the transaction type to search for
     * @param pageable pagination information
     * @return a page of transactions for the specified user and transaction type
     */
    Page<Transaction> findByUserAndType(User user, TransactionType type, Pageable pageable);

    /**
     * Find all transactions for a specific user ID.
     *
     * @param userId the ID of the user to search for
     * @param pageable pagination information
     * @return a page of transactions for the specified user ID
     */
    Page<Transaction> findByUserId(UUID userId, Pageable pageable);

    /**
     * Find all transactions for a specific user ID and transaction type.
     *
     * @param userId the ID of the user to search for
     * @param type the transaction type to search for
     * @param pageable pagination information
     * @return a page of transactions for the specified user ID and transaction type
     */
    Page<Transaction> findByUserIdAndType(UUID userId, TransactionType type, Pageable pageable);

    /**
     * Find all transactions for a specific user ID, transaction type, and time range.
     *
     * @param userId the ID of the user to search for
     * @param type the transaction type to search for
     * @param startTime the start of the time range
     * @param endTime the end of the time range
     * @param pageable pagination information
     * @return a page of transactions for the specified user ID, transaction type, and time range
     */
    Page<Transaction> findByUserIdAndTypeAndCreatedAtBetween(
            UUID userId, 
            TransactionType type, 
            LocalDateTime startTime, 
            LocalDateTime endTime, 
            Pageable pageable);

    /**
     * Find all transactions that occurred within a specific time range.
     *
     * @param startTime the start of the time range
     * @param endTime the end of the time range
     * @param pageable pagination information
     * @return a page of transactions that occurred within the specified time range
     */
    Page<Transaction> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * Find all transactions for a specific reference ID.
     *
     * @param referenceId the reference ID to search for
     * @return a list of transactions for the specified reference ID
     */
    List<Transaction> findByReferenceId(UUID referenceId);

    /**
     * Calculate the total amount of transactions for a specific user and transaction type.
     *
     * @param userId the ID of the user
     * @param type the transaction type
     * @return the total amount of transactions for the specified user and transaction type
     */
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId AND t.type = :type")
    Long sumAmountByUserIdAndType(@Param("userId") UUID userId, @Param("type") TransactionType type);

    /**
     * Calculate the total amount of transactions for a specific user, transaction type, and time range.
     *
     * @param userId the ID of the user
     * @param type the transaction type
     * @param startTime the start of the time range
     * @param endTime the end of the time range
     * @return the total amount of transactions for the specified user, transaction type, and time range
     */
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId AND t.type = :type AND t.createdAt BETWEEN :startTime AND :endTime")
    Long sumAmountByUserIdAndTypeAndTimeRange(
            @Param("userId") UUID userId,
            @Param("type") TransactionType type,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}
