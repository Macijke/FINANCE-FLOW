package finance_flow.Finance_Flow.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Builder
public record AnalyticsResponse(
        FinancialSummary summary,
        List<CategoryBreakdown> categoryBreakdowns,
        List<MonthlyTrend> monthlyTrends,
        BudgetOverview budgetOverview,
        List<SpendingInsight> insights
) {
    @Builder
    public record FinancialSummary(
            BigDecimal totalIncome,
            BigDecimal averageIncome,
            BigDecimal totalExpenses,
            BigDecimal averageExpenses,
            BigDecimal netBalance,
            BigDecimal totalBalance,
            BigDecimal savingsRate,
            Integer expansiveTransactionsCount,
            Integer incomeTransactionsCount,
            BigDecimal differenceFromPreviousPeriod,
            LocalDate periodStart,
            LocalDate periodEnd
    ) {

    }

    @Builder
    public record CategoryBreakdown(
            Long categoryId,
            String categoryName,
            String categoryIcon,
            String categoryColor,
            BigDecimal amount,
            BigDecimal percentage,
            Integer transactionCount
    ) {

    }

    @Builder
    public record MonthlyTrend(
            String month,
            BigDecimal income,
            BigDecimal expenses,
            BigDecimal balance
    ) {

    }

    @Builder
    public record BudgetOverview(
            BigDecimal totalBudget,
            BigDecimal totalSpent,
            BigDecimal remaining,
            BigDecimal utilizationRate,
            List<BudgetStatus> budgetStatuses
    ) {

    }

    @Builder
    public record BudgetStatus(
            Long budgetId,
            Long categoryId,
            String categoryName,
            BigDecimal budgetLimit,
            BigDecimal spent,
            BigDecimal remaining,
            BigDecimal utilizationRate,
            String status
    ) {

    }

    @Builder
    public record SpendingInsight(
            String type,
            String message,
            String severity,
            Map<String, Object> metadata
    ) {

    }
}