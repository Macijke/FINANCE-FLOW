package finance_flow.Finance_Flow.controller;

import finance_flow.Finance_Flow.dto.request.TransactionRequest;
import finance_flow.Finance_Flow.dto.response.ApiResponse;
import finance_flow.Finance_Flow.dto.response.TransactionResponse;
import finance_flow.Finance_Flow.model.enums.TransactionType;
import finance_flow.Finance_Flow.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Transaction management APIs")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create new transaction", description = "Creates a new income or expense transaction")
    public ResponseEntity<ApiResponse<TransactionResponse>> createTransaction(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.createTransaction(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<TransactionResponse>builder().success(true).message("Transaction created successfully").data(response).build());
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get all transactions", description = "Get all transactions with filtering and pagination")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getAllTransactions(@RequestParam(required = false) Long categoryId, @RequestParam(required = false) TransactionType type, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate, @PageableDefault(size = 20, sort = "transactionDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TransactionResponse> transactions = transactionService.getAllTransactions(categoryId, type, startDate, endDate, pageable);

        return ResponseEntity.ok(ApiResponse.<Page<TransactionResponse>>builder().success(true).message("Transactions retrieved successfully").data(transactions).build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get transaction by ID", description = "Retrieve a specific transaction by its ID")
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransactionById(@PathVariable Long id) {
        TransactionResponse response = transactionService.getTransactionById(id);

        return ResponseEntity.ok(ApiResponse.<TransactionResponse>builder().success(true).message("Transaction retrieved successfully").data(response).build());
    }

    @GetMapping("/recent")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get recent transactions", description = "Get the most recent N transactions")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getRecentTransactions(@RequestParam(defaultValue = "5") int limit) {
        List<TransactionResponse> transactions = transactionService.getRecentTransactions(limit);

        return ResponseEntity.ok(ApiResponse.<List<TransactionResponse>>builder().success(true).message("Recent transactions retrieved successfully").data(transactions).build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update transaction", description = "Update an existing transaction")
    public ResponseEntity<ApiResponse<TransactionResponse>> updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.updateTransaction(id, request);

        return ResponseEntity.ok(ApiResponse.<TransactionResponse>builder().success(true).message("Transaction updated successfully").data(response).build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Delete transaction", description = "Delete a transaction by ID")
    public ResponseEntity<ApiResponse<Void>> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);

        return ResponseEntity.ok(ApiResponse.<Void>builder().success(true).message("Transaction deleted successfully").build());
    }
}

