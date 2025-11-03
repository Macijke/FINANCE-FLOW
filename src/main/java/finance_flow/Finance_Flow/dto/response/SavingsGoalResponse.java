package finance_flow.Finance_Flow.dto.response;

import finance_flow.Finance_Flow.model.enums.GoalStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingsGoalResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private BigDecimal remainingAmount;
    private Integer percentageCompleted;
    private LocalDate targetDate;
    private GoalStatus status;
    private String icon;
    private String color;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
