package finance_flow.Finance_Flow.service;

import finance_flow.Finance_Flow.dto.request.SavingsGoalRequest;
import finance_flow.Finance_Flow.dto.response.SavingsGoalResponse;
import finance_flow.Finance_Flow.model.enums.GoalStatus;
import java.util.List;

public interface SavingsGoalService {

    SavingsGoalResponse createGoal(SavingsGoalRequest request);

    SavingsGoalResponse updateGoal(Long id, SavingsGoalRequest request);

    void deleteGoal(Long id);

    SavingsGoalResponse getGoalById(Long id);

    List<SavingsGoalResponse> getAllGoals();

    List<SavingsGoalResponse> getGoalsByStatus(GoalStatus status);

    SavingsGoalResponse contributeToGoal(Long id, java.math.BigDecimal amount);

    SavingsGoalResponse completeGoal(Long id);
}
