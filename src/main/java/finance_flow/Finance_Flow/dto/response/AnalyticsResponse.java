package finance_flow.Finance_Flow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsResponse {
    private LocalDateTime lastMonth;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpense;
    private BigDecimal totalBalance;
    private Integer savingsGoalProgress;
    
}
