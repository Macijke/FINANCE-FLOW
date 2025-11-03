package finance_flow.Finance_Flow.controller;

import finance_flow.Finance_Flow.dto.request.BudgetRequest;
import finance_flow.Finance_Flow.dto.response.ApiResponse;
import finance_flow.Finance_Flow.dto.response.BudgetResponse;
import finance_flow.Finance_Flow.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<BudgetResponse>> createBudget(
            @Valid @RequestBody BudgetRequest request) {
        BudgetResponse budgetResponse = budgetService.createBudget(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<BudgetResponse>builder()
                        .success(true)
                        .message("Budget created successfully")
                        .data(budgetResponse)
                        .build());

    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<List<BudgetResponse>>> getAllBudgets() {
        List<BudgetResponse> allBudgets = budgetService.getAllBudgets();
        return ResponseEntity.ok(
                ApiResponse.<List<BudgetResponse>>builder()
                        .success(true)
                        .message("Budgets retrieved successfully")
                        .data(allBudgets)
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<BudgetResponse>> getBudgetById(@PathVariable Long id) {
        BudgetResponse budgetResponse = budgetService.getBudgetById(id);
        return ResponseEntity.ok(
                ApiResponse.<BudgetResponse>builder()
                        .success(true)
                        .message("Budget retrieved successfully")
                        .data(budgetResponse)
                        .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<BudgetResponse>> updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest request
    ) {
        BudgetResponse budgetResponse = budgetService.updateBudget(id, request);

        return ResponseEntity.ok(
                ApiResponse.<BudgetResponse>builder()
                        .success(true)
                        .message("Budget updated successfully")
                        .data(budgetResponse)
                        .build()
        );
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Budget deleted successfully")
                        .build()
        );
    }

    @GetMapping("/month")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<List<BudgetResponse>>> getBudgetsByMonth(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month
    ) {
        List<BudgetResponse> budgets = budgetService.getBudgetsByMonth(month);

        return ResponseEntity.ok(
                ApiResponse.<List<BudgetResponse>>builder()
                        .success(true)
                        .message("Budgets for the month retrieved successfully")
                        .data(budgets)
                        .build()
        );
    }

    @GetMapping("/{id}/status")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<BudgetResponse>> checkBudgetStatus(@PathVariable Long id) {
        BudgetResponse budgetStatus = budgetService.checkBudgetStatus(id);
        return ResponseEntity.ok(
                ApiResponse.<BudgetResponse>builder()
                        .success(true)
                        .message("Budget status retrieved successfully")
                        .data(budgetStatus)
                        .build()
        );
    }
}
