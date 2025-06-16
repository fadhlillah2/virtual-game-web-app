package co.id.virtual.game.web.app.controller;

import co.id.virtual.game.web.app.dto.ApiResponse;
import co.id.virtual.game.web.app.dto.chip.ChipBalanceDto;
import co.id.virtual.game.web.app.dto.chip.DailyBonusResponse;
import co.id.virtual.game.web.app.dto.chip.GiftResponse;
import co.id.virtual.game.web.app.dto.chip.SendGiftRequest;
import co.id.virtual.game.web.app.dto.chip.TransactionDto;
import co.id.virtual.game.web.app.exception.DailyBonusAlreadyClaimedException;
import co.id.virtual.game.web.app.exception.DailyGiftLimitExceededException;
import co.id.virtual.game.web.app.exception.InsufficientChipsException;
import co.id.virtual.game.web.app.model.TransactionType;
import co.id.virtual.game.web.app.security.UserPrincipal;
import co.id.virtual.game.web.app.service.ChipService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling chip operations.
 */
@RestController
@RequestMapping("/api/chips")
@PreAuthorize("hasRole('USER')")
@Slf4j
public class ChipController {
    
    private final ChipService chipService;
    
    @Autowired
    public ChipController(ChipService chipService) {
        this.chipService = chipService;
    }
    
    /**
     * Get the current user's chip balance.
     *
     * @param userPrincipal the authenticated user
     * @return the user's chip balance
     */
    @GetMapping("/balance")
    public ResponseEntity<ApiResponse<ChipBalanceDto>> getBalance(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        ChipBalanceDto balance = chipService.getUserBalance(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Balance retrieved successfully", balance));
    }
    
    /**
     * Claim the daily bonus for the current user.
     *
     * @param userPrincipal the authenticated user
     * @return the daily bonus response
     */
    @PostMapping("/daily-bonus")
    public ResponseEntity<ApiResponse<DailyBonusResponse>> claimDailyBonus(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        try {
            DailyBonusResponse response = chipService.claimDailyBonus(userPrincipal.getId());
            
            log.info("User {} claimed daily bonus: {} chips", 
                    userPrincipal.getUsername(), response.getBonusAmount());
            
            return ResponseEntity.ok(ApiResponse.success("Daily bonus claimed successfully", response));
        } catch (DailyBonusAlreadyClaimedException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Daily bonus already claimed today"));
        }
    }
    
    /**
     * Send a gift to another user.
     *
     * @param userPrincipal the authenticated user
     * @param request the gift request
     * @return the gift response
     */
    @PostMapping("/gift")
    public ResponseEntity<ApiResponse<GiftResponse>> sendGift(
            @Valid @RequestBody SendGiftRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        try {
            GiftResponse response = chipService.sendGift(
                userPrincipal.getId(), 
                request.getRecipientId(), 
                request.getAmount()
            );
            
            log.info("User {} sent {} chips to user {}", 
                    userPrincipal.getUsername(), request.getAmount(), request.getRecipientId());
            
            return ResponseEntity.ok(ApiResponse.success("Gift sent successfully", response));
        } catch (InsufficientChipsException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Insufficient chips"));
        } catch (DailyGiftLimitExceededException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Daily gift limit exceeded"));
        }
    }
    
    /**
     * Get the current user's transaction history.
     *
     * @param userPrincipal the authenticated user
     * @param page the page number
     * @param size the page size
     * @param type the transaction type filter (optional)
     * @return a page of the user's transaction history
     */
    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<Page<TransactionDto>>> getTransactionHistory(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) TransactionType type) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TransactionDto> transactions = chipService.getTransactionHistory(
            userPrincipal.getId(), 
            type, 
            pageable
        );
        
        return ResponseEntity.ok(ApiResponse.success("Transaction history retrieved successfully", transactions));
    }
}
