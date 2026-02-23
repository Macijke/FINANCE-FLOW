package finance_flow.Finance_Flow.dto.request;

import finance_flow.Finance_Flow.model.enums.TransactionType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
        @NotNull(message = "Category ID is required")
        Long categoryId,
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        @Digits(integer = 8, fraction = 2, message = "Amount must have max 8 digits and 2 decimals")
        BigDecimal amount,
        @NotNull(message = "Transaction type is required")
        TransactionType type,
        @Size(max = 255, message = "Description must not exceed 255 characters")
        String description,
        @NotNull(message = "Transaction date is required")
        @PastOrPresent(message = "Transaction date cannot be in the future")
        LocalDate transactionDate
) {

}