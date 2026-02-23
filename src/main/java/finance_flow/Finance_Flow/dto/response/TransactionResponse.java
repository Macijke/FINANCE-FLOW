package finance_flow.Finance_Flow.dto.response;

import finance_flow.Finance_Flow.model.enums.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record TransactionResponse(
        Long id,
        String categoryName,
        String categoryColor,
        BigDecimal amount,
        TransactionType type,
        String description,
        Long categoryId,
        String categoryIcon,
        LocalDate transactionDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}