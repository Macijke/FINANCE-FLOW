package finance_flow.Finance_Flow.controller;

import finance_flow.Finance_Flow.dto.request.ContributeRequest;
import finance_flow.Finance_Flow.dto.request.SavingsGoalRequest;
import finance_flow.Finance_Flow.dto.response.ApiResponse;
import finance_flow.Finance_Flow.dto.response.SavingsGoalResponse;
import finance_flow.Finance_Flow.model.enums.GoalStatus;
import finance_flow.Finance_Flow.service.SavingsGoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/savings-goals")
@RequiredArgsConstructor
@Tag(name = "Savings Goals", description = "Savings Goal management APIs")
public class SavingsGoalController {

    private final SavingsGoalService savingsGoalService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create new savings goal", description = "Creates a new savings goal")
    public ResponseEntity<ApiResponse<SavingsGoalResponse>> createSavingsGoal(
            @Valid @RequestBody SavingsGoalRequest request
    ) {
        SavingsGoalResponse response = savingsGoalService.createGoal(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<SavingsGoalResponse>builder()
                        .success(true)
                        .message("Savings goal created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get all savings goals", description = "Retrieve all savings goals for the current user")
    public ResponseEntity<ApiResponse<List<SavingsGoalResponse>>> getAllSavingsGoals() {
        List<SavingsGoalResponse> responses = savingsGoalService.getAllGoals();

        return ResponseEntity.ok(
                ApiResponse.<List<SavingsGoalResponse>>builder()
                        .success(true)
                        .message("Savings goals retrieved successfully")
                        .data(responses)
                        .build()
        );
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get savings goals by status", description = "Retrieve savings goals by status (ACTIVE, COMPLETED, CANCELLED)")
    public ResponseEntity<ApiResponse<List<SavingsGoalResponse>>> getSavingsGoalsByStatus(
            @PathVariable String status
    ) {
        List<SavingsGoalResponse> responses = savingsGoalService.getGoalsByStatus(GoalStatus.valueOf(status));

        return ResponseEntity.ok(
                ApiResponse.<List<SavingsGoalResponse>>builder()
                        .success(true)
                        .message("Savings goals retrieved successfully")
                        .data(responses)
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get savings goal by ID", description = "Retrieve a specific savings goal by its ID")
    public ResponseEntity<ApiResponse<SavingsGoalResponse>> getSavingsGoalById(
            @PathVariable Long id
    ) {
        SavingsGoalResponse response = savingsGoalService.getGoalById(id);

        return ResponseEntity.ok(
                ApiResponse.<SavingsGoalResponse>builder()
                        .success(true)
                        .message("Savings goal retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update savings goal", description = "Updates an existing savings goal")
    public ResponseEntity<ApiResponse<SavingsGoalResponse>> updateSavingsGoal(
            @PathVariable Long id,
            @Valid @RequestBody SavingsGoalRequest request
    ) {
        SavingsGoalResponse response = savingsGoalService.updateGoal(id, request);

        return ResponseEntity.ok(
                ApiResponse.<SavingsGoalResponse>builder()
                        .success(true)
                        .message("Savings goal updated successfully")
                        .data(response)
                        .build()
        );
    }

    @PostMapping("/{id}/contribute")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Contribute to savings goal", description = "Contributes an amount to the specified savings goal")
    public ResponseEntity<ApiResponse<SavingsGoalResponse>> contributeToSavingsGoal(
            @PathVariable Long id,
            @Valid @RequestBody ContributeRequest request
    ) {
        SavingsGoalResponse response = savingsGoalService.contributeToGoal(id, request.getAmount());

        return ResponseEntity.ok(
                ApiResponse.<SavingsGoalResponse>builder()
                        .success(true)
                        .message("Contribution to savings goal successful")
                        .data(response)
                        .build()
        );
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Complete savings goal", description = "Marks the specified savings goal as complete")
    public ResponseEntity<ApiResponse<SavingsGoalResponse>> completeSavingsGoal(
            @PathVariable Long id
    ) {
        SavingsGoalResponse response = savingsGoalService.completeGoal(id);

        return ResponseEntity.ok(
                ApiResponse.<SavingsGoalResponse>builder()
                        .success(true)
                        .message("Savings goal completed successfully")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Delete savings goal", description = "Deletes the specified savings goal")
    public ResponseEntity<ApiResponse<Void>> deleteSavingsGoal(
            @PathVariable Long id
    ) {
        savingsGoalService.deleteGoal(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Savings goal deleted successfully")
                        .build()
        );
    }
}
