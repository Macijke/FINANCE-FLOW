package finance_flow.Finance_Flow.service;

import finance_flow.Finance_Flow.dto.request.BudgetRequest;
import finance_flow.Finance_Flow.dto.response.BudgetResponse;

import java.time.LocalDate;
import java.util.List;

public interface BudgetService {

    BudgetResponse createBudget(BudgetRequest request);

    BudgetResponse updateBudget(Long id, BudgetRequest request);

    void deleteBudget(Long id);

    BudgetResponse getBudgetById(Long id);

    List<BudgetResponse> getAllBudgets();

    List<BudgetResponse> getBudgetsByMonth(LocalDate month);

    BudgetResponse checkBudgetStatus(Long id);
}
