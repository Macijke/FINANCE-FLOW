package finance_flow.Finance_Flow.dto.response;

import finance_flow.Finance_Flow.model.enums.GoalStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record SavingsGoalResponse(
        Long id,
        String name,
        String description,
        BigDecimal targetAmount,
        BigDecimal currentAmount,
        BigDecimal remainingAmount,
        Integer percentageCompleted,
        LocalDate targetDate,
        GoalStatus status,
        String icon,
        String color,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}