package finance_flow.Finance_Flow.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record BudgetRequest(
        Long categoryId,
        @NotNull
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        @Digits(integer = 8, fraction = 2, message = "Amount must have max 8 digits and 2 decimals")
        BigDecimal limitAmount,
        @NotNull(message = "Month is required")
        LocalDate month,
        Boolean alertEnabled
) {
    public BudgetRequest {
        if (alertEnabled == null) alertEnabled = true;
    }
}