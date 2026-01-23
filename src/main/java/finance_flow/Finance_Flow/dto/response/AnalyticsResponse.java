package finance_flow.Finance_Flow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {
    private FinancialSummary summary;
    private List<CategoryBreakdown> categoryBreakdowns;
    private List<MonthlyTrend> monthlyTrends;
    private BudgetOverview budgetOverview;
    private List<SpendingInsight> insights;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FinancialSummary {
        private BigDecimal totalIncome;
        private BigDecimal totalExpenses;
        private BigDecimal netBalance;
        private BigDecimal totalBalance;
        private BigDecimal savingsRate;
        private Integer expansiveTransactionsCount;
        private Integer incomeTransactionsCount;
        private BigDecimal differenceFromPreviousPeriod;
        private LocalDate periodStart;
        private LocalDate periodEnd;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryBreakdown {
        private Long categoryId;
        private String categoryName;
        private String categoryIcon;
        private String categoryColor;
        private BigDecimal amount;
        private BigDecimal percentage;
        private Integer transactionCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyTrend {
        private String month;
        private BigDecimal income;
        private BigDecimal expenses;
        private BigDecimal balance;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BudgetOverview {
        private BigDecimal totalBudget;
        private BigDecimal totalSpent;
        private BigDecimal remaining;
        private BigDecimal utilizationRate;
        private List<BudgetStatus> budgetStatuses;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BudgetStatus {
        private Long budgetId;
        private Long categoryId;
        private String categoryName;
        private BigDecimal budgetLimit;
        private BigDecimal spent;
        private BigDecimal remaining;
        private BigDecimal utilizationRate;
        private String status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpendingInsight {
        private String type;
        private String message;
        private String severity;
        private Map<String, Object> metadata;
    }


}


