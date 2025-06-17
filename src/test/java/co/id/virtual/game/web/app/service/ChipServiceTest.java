package co.id.virtual.game.web.app.service;

import co.id.virtual.game.web.app.dto.chip.ChipBalanceDto;
import co.id.virtual.game.web.app.model.TransactionType;
import co.id.virtual.game.web.app.model.User;
import co.id.virtual.game.web.app.repository.TransactionRepository;
import co.id.virtual.game.web.app.repository.UserRepository;
import co.id.virtual.game.web.app.service.impl.ChipServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.mockito.ArgumentMatchers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the ChipService implementation.
 * This demonstrates how to create tests for the service layer using Mockito.
 */
@ExtendWith(MockitoExtension.class)
public class ChipServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private ChipServiceImpl chipService;

    private User testUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        // Create a test user
        userId = UUID.randomUUID();
        testUser = User.builder()
                .id(userId)
                .username("testuser")
                .email("test@example.com")
                .passwordHash("hashedpassword")
                .birthDate(LocalDate.of(1990, 1, 1))
                .chipsBalance(1000L)
                .totalChipsWon(500L)
                .build();

        // Use lenient stubbing to avoid strict stubbing issues
        Mockito.lenient().when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // Create an empty page for mocking repository methods
        Page<Object> emptyPage = new PageImpl<>(Collections.emptyList());

        // Mock all repository methods that are called in getUserBalance
        Mockito.lenient().when(transactionRepository.findByUserId(userId, Pageable.ofSize(1))).thenReturn((Page) emptyPage);
        Mockito.lenient().when(transactionRepository.findByUserIdAndType(
                ArgumentMatchers.eq(userId),
                ArgumentMatchers.any(TransactionType.class),
                ArgumentMatchers.any(Pageable.class)
        )).thenReturn((Page) emptyPage);

        Mockito.lenient().when(transactionRepository.findByUserIdAndTypeAndCreatedAtBetween(
                ArgumentMatchers.eq(userId),
                ArgumentMatchers.any(TransactionType.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any(Pageable.class)
        )).thenReturn((Page) emptyPage);
    }

    @Test
    void getUserBalance_ShouldReturnCorrectBalance() {
        // Arrange - all mocking is done in setUp

        System.out.println("[DEBUG_LOG] Testing getUserBalance with user ID: " + userId);

        // Act
        ChipBalanceDto balance = chipService.getUserBalance(userId);

        System.out.println("[DEBUG_LOG] Retrieved balance: " + balance.getChipsBalance());

        // Assert
        assertNotNull(balance);
        assertEquals(userId, balance.getUserId());
        assertEquals(1000L, balance.getChipsBalance());
        assertEquals(500L, balance.getTotalChipsWon());
    }
}
