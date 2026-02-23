package finance_flow.Finance_Flow.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record BudgetResponse(
        Long id,
        Long categoryId,
        String categoryName,
        String categoryColor,
        String categoryIcon,
        BigDecimal limitAmount,
        LocalDate month,
        Boolean alertEnabled,
        BigDecimal spentAmount,
        BigDecimal remainingAmount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer percentageUsed
) {

}