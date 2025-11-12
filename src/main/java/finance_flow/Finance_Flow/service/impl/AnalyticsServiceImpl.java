package finance_flow.Finance_Flow.service.impl;

import finance_flow.Finance_Flow.dto.response.AnalyticsResponse;
import finance_flow.Finance_Flow.model.Budget;
import finance_flow.Finance_Flow.model.Category;
import finance_flow.Finance_Flow.model.User;
import finance_flow.Finance_Flow.model.enums.TransactionType;
import finance_flow.Finance_Flow.repository.BudgetRepository;
import finance_flow.Finance_Flow.repository.CategoryRepository;
import finance_flow.Finance_Flow.repository.TransactionRepository;
import finance_flow.Finance_Flow.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsServiceImpl {

    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;

    public AnalyticsResponse getAnalytics(LocalDate startDate,
                                          LocalDate endDate) {

        return AnalyticsResponse.builder()
                .summary(buildFinancialSummary(startDate, endDate))
                .categoryBreakdowns(buildCategoryBreakdowns(startDate, endDate))
                .monthlyTrends(buildMonthlyTrends(startDate, endDate))
                .budgetOverview(buildBudgetOverview(YearMonth.now().atDay(1)))
                .insights(generateInsights(startDate, endDate))
                .build();
    }

    private AnalyticsResponse.FinancialSummary buildFinancialSummary(LocalDate startDate,
                                                                     LocalDate endDate) {
        User currentUser = SecurityUtils.getCurrentUser();
        Long userId = currentUser.getId();

        BigDecimal totalIncome = transactionRepository.sumByUserAndTypeAndDateRange(
                userId, TransactionType.INCOME, startDate, endDate);

        BigDecimal totalExpenses = transactionRepository.sumByUserAndTypeAndDateRange(
                userId, TransactionType.EXPENSE, startDate, endDate);

        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        BigDecimal totalBalance = transactionRepository.calculateTotalBalance(currentUser);

        BigDecimal savingsRate = totalIncome.compareTo(BigDecimal.ZERO) > 0
                ? netBalance.divide(totalIncome, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        return AnalyticsResponse.FinancialSummary.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .netBalance(netBalance)
                .totalBalance(totalBalance)
                .savingsRate(savingsRate)
                .periodStart(startDate)
                .periodEnd(endDate)
                .build();
    }

    private List<AnalyticsResponse.CategoryBreakdown> buildCategoryBreakdowns(
            LocalDate startDate,
            LocalDate endDate) {
        Long userId = SecurityUtils.getCurrentUserId();
        List<Object[]> categoryData = transactionRepository.sumByCategoryAndDateRange(
                userId, TransactionType.EXPENSE, startDate, endDate);

        BigDecimal totalExpenses = categoryData.stream()
                .map(row -> (BigDecimal) row[1])
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return categoryData.stream()
                .map(row -> {
                    Long categoryId = (Long) row[0];
                    BigDecimal amount = (BigDecimal) row[1];

                    Category category = categoryRepository.findById(Math.toIntExact(categoryId))
                            .orElseThrow();

                    BigDecimal percentage = totalExpenses.compareTo(BigDecimal.ZERO) > 0
                            ? amount.divide(totalExpenses, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            : BigDecimal.ZERO;

                    Long transactionCount = transactionRepository.countByCategoryAndTypeAndDateRange(
                            userId, categoryId, TransactionType.EXPENSE, startDate, endDate);

                    return AnalyticsResponse.CategoryBreakdown.builder()
                            .categoryId(categoryId)
                            .categoryName(category.getName())
                            .categoryIcon(category.getIcon())
                            .categoryColor(category.getColor())
                            .amount(amount)
                            .percentage(percentage)
                            .transactionCount(transactionCount.intValue())
                            .build();
                })
                .sorted(Comparator.comparing(AnalyticsResponse.CategoryBreakdown::getAmount).reversed())
                .collect(Collectors.toList());
    }

    private List<AnalyticsResponse.MonthlyTrend> buildMonthlyTrends(
            LocalDate startDate,
            LocalDate endDate) {
        Long userId = SecurityUtils.getCurrentUserId();
        List<Object[]> trends = transactionRepository.getMonthlyTrends(
                userId, startDate, endDate);

        Map<String, AnalyticsResponse.MonthlyTrend> trendMap = new HashMap<>();

        for (Object[] row : trends) {
            String month = (String) row[0];
            TransactionType type = (TransactionType) row[1];
            BigDecimal amount = (BigDecimal) row[2];

            AnalyticsResponse.MonthlyTrend trend = trendMap.computeIfAbsent(month,
                    m -> AnalyticsResponse.MonthlyTrend.builder()
                            .month(m)
                            .income(BigDecimal.ZERO)
                            .expenses(BigDecimal.ZERO)
                            .balance(BigDecimal.ZERO)
                            .build());

            if (type == TransactionType.INCOME) {
                trend.setIncome(amount);
            } else {
                trend.setExpenses(amount);
            }
        }

        trendMap.values().forEach(trend ->
                trend.setBalance(trend.getIncome().subtract(trend.getExpenses())));

        return trendMap.values().stream()
                .sorted(Comparator.comparing(AnalyticsResponse.MonthlyTrend::getMonth))
                .collect(Collectors.toList());
    }

    private AnalyticsResponse.BudgetOverview buildBudgetOverview(LocalDate month) {
        User currentUser = SecurityUtils.getCurrentUser();
        List<Budget> budgets = budgetRepository.findActiveBudgetsForMonth(currentUser, month);

        if (budgets.isEmpty()) {
            return AnalyticsResponse.BudgetOverview.builder()
                    .totalBudget(BigDecimal.ZERO)
                    .totalSpent(BigDecimal.ZERO)
                    .remaining(BigDecimal.ZERO)
                    .utilizationRate(BigDecimal.ZERO)
                    .budgetStatuses(Collections.emptyList())
                    .build();
        }

        YearMonth yearMonth = YearMonth.from(month);

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        BigDecimal totalBudget = BigDecimal.ZERO;
        BigDecimal totalSpent = BigDecimal.ZERO;
        List<AnalyticsResponse.BudgetStatus> statuses = new ArrayList<>();

        for (Budget budget : budgets) {
            BigDecimal spent = transactionRepository.sumByCategoryAndDateRange(
                            currentUser.getId(),
                            TransactionType.EXPENSE,
                            startDate,
                            endDate
                    ).stream()
                    .filter(row -> row[0].equals(budget.getCategory().getId()))
                    .map(row -> (BigDecimal) row[1])
                    .findFirst()
                    .orElse(BigDecimal.ZERO);

            BigDecimal remaining = budget.getLimitAmount().subtract(spent);
            BigDecimal utilization = budget.getLimitAmount().compareTo(BigDecimal.ZERO) > 0
                    ? spent.divide(budget.getLimitAmount(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    : BigDecimal.ZERO;

            String status = determineStatus(utilization);

            statuses.add(AnalyticsResponse.BudgetStatus.builder()
                    .budgetId(budget.getId())
                    .categoryId(budget.getCategory().getId())
                    .categoryName(budget.getCategory().getName())
                    .budgetLimit(budget.getLimitAmount())
                    .spent(spent)
                    .remaining(remaining)
                    .utilizationRate(utilization)
                    .status(status)
                    .build());

            totalBudget = totalBudget.add(budget.getLimitAmount());
            totalSpent = totalSpent.add(spent);
        }

        BigDecimal remaining = totalBudget.subtract(totalSpent);
        BigDecimal utilizationRate = totalBudget.compareTo(BigDecimal.ZERO) > 0
                ? totalSpent.divide(totalBudget, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        return AnalyticsResponse.BudgetOverview.builder()
                .totalBudget(totalBudget)
                .totalSpent(totalSpent)
                .remaining(remaining)
                .utilizationRate(utilizationRate)
                .budgetStatuses(statuses)
                .build();
    }

    private String determineStatus(BigDecimal utilizationRate) {
        if (utilizationRate.compareTo(BigDecimal.valueOf(100)) >= 0) {
            return "EXCEEDED";
        } else if (utilizationRate.compareTo(BigDecimal.valueOf(80)) >= 0) {
            return "WARNING";
        } else {
            return "SAFE";
        }
    }

    private List<AnalyticsResponse.SpendingInsight> generateInsights(
            LocalDate startDate,
            LocalDate endDate) {
        List<AnalyticsResponse.SpendingInsight> insights = new ArrayList<>();

        List<AnalyticsResponse.CategoryBreakdown> categories = buildCategoryBreakdowns(startDate, endDate);


        if (!categories.isEmpty()) {
            AnalyticsResponse.CategoryBreakdown topCategory = categories.get(0);
            insights.add(AnalyticsResponse.SpendingInsight.builder()
                    .type("TOP_CATEGORY")
                    .message(String.format("Najwięcej wydajesz na: %s (%.2f%%)",
                            topCategory.getCategoryName(),
                            topCategory.getPercentage()))
                    .severity("INFO")
                    .metadata(Map.of(
                            "categoryId", topCategory.getCategoryId(),
                            "amount", topCategory.getAmount(),
                            "percentage", topCategory.getPercentage()
                    ))
                    .build());
        }

        AnalyticsResponse.BudgetOverview budgetOverview = buildBudgetOverview(LocalDate.now());
        budgetOverview.getBudgetStatuses().stream()
                .filter(status -> "EXCEEDED".equals(status.getStatus()) ||
                        "WARNING".equals(status.getStatus()))
                .forEach(status -> {
                    String severity = "EXCEEDED".equals(status.getStatus())
                            ? "CRITICAL" : "WARNING";
                    String message = "EXCEEDED".equals(status.getStatus())
                            ? String.format("Przekroczono budżet dla: %s", status.getCategoryName())
                            : String.format("Uwaga! Zbliżasz się do limitu w kategorii: %s",
                            status.getCategoryName());

                    insights.add(AnalyticsResponse.SpendingInsight.builder()
                            .type("BUDGET_ALERT")
                            .message(message)
                            .severity(severity)
                            .metadata(Map.of(
                                    "budgetId", status.getBudgetId(),
                                    "categoryId", status.getCategoryId(),
                                    "utilizationRate", status.getUtilizationRate()
                            ))
                            .build());
                });

        return insights;
    }
}
