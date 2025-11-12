package finance_flow.Finance_Flow.service;

import finance_flow.Finance_Flow.dto.response.AnalyticsResponse;

import java.time.LocalDate;
import java.util.List;

public interface AnalyticsService {

    AnalyticsResponse getAnalytics(LocalDate startDate,
                                   LocalDate endDate);

    AnalyticsResponse.FinancialSummary buildFinancialSummary(LocalDate startDate,
                                                             LocalDate endDate);

    List<AnalyticsResponse.CategoryBreakdown> buildCategoryBreakdowns(
            LocalDate startDate,
            LocalDate endDate);

    List<AnalyticsResponse.MonthlyTrend> buildMonthlyTrends(
            LocalDate startDate,
            LocalDate endDate);

    AnalyticsResponse.BudgetOverview buildBudgetOverview(LocalDate month);
}
