package finance_flow.Finance_Flow.controller;

import finance_flow.Finance_Flow.dto.response.AnalyticsResponse;
import finance_flow.Finance_Flow.dto.response.ApiResponse;
import finance_flow.Finance_Flow.service.impl.AnalyticsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class AnalyticsController {

    private final AnalyticsServiceImpl analyticsService;

    @GetMapping
    public ResponseEntity<ApiResponse<AnalyticsResponse>> getAnalytics(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (startDate == null || endDate == null) {
            YearMonth currentMonth = YearMonth.now();
            startDate = currentMonth.atDay(1);
            endDate = currentMonth.atEndOfMonth();
        }

        AnalyticsResponse analytics = analyticsService.getAnalytics(startDate, endDate);

        return ResponseEntity.ok(ApiResponse.<AnalyticsResponse>builder()
                .success(true)
                .message("Analytics retrieved successfully")
                .data(analytics)
                .build());
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<AnalyticsResponse.FinancialSummary>> getSummary(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        if (startDate == null || endDate == null) {
            YearMonth currentMonth = YearMonth.now();
            startDate = currentMonth.atDay(1);
            endDate = currentMonth.atEndOfMonth();
        }

        AnalyticsResponse analytics = analyticsService.getAnalytics(startDate, endDate);

        return ResponseEntity.ok(ApiResponse.<AnalyticsResponse.FinancialSummary>builder()
                .success(true)
                .message("Financial summary retrieved successfully")
                .data(analytics.getSummary())
                .build());
    }

    @GetMapping("/monthly-trends")
    public ResponseEntity<ApiResponse<List<AnalyticsResponse.MonthlyTrend>>> getMonthlyTrends(
            @RequestParam(defaultValue = "6") int months
    ) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(months);

        AnalyticsResponse analytics = analyticsService.getAnalytics(startDate, endDate);

        return ResponseEntity.ok(ApiResponse.<List<AnalyticsResponse.MonthlyTrend>>builder()
                .success(true)
                .message("Monthly trends retrieved successfully")
                .data(analytics.getMonthlyTrends())
                .build());
    }

    @GetMapping("/category-breakdown")
    public ResponseEntity<ApiResponse<List<AnalyticsResponse.CategoryBreakdown>>> getCategoryBreakdown(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate == null || endDate == null) {
            YearMonth currentMonth = YearMonth.now();
            startDate = currentMonth.atDay(1);
            endDate = currentMonth.atEndOfMonth();
        }
        AnalyticsResponse analytics = analyticsService.getAnalytics(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.<List<AnalyticsResponse.CategoryBreakdown>>builder()
                .success(true)
                .message("Category breakdown retrieved successfully")
                .data(analytics.getCategoryBreakdowns())
                .build());
    }
}
