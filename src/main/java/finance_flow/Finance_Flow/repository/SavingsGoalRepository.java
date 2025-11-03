package finance_flow.Finance_Flow.repository;

import finance_flow.Finance_Flow.model.SavingsGoal;
import finance_flow.Finance_Flow.model.User;
import finance_flow.Finance_Flow.model.enums.GoalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {

    Optional<SavingsGoal> findByIdAndUser(Long id, User user);

    List<SavingsGoal> findByUser(User user);

    List<SavingsGoal> findByUserAndStatus(User user, GoalStatus status);

    List<SavingsGoal> findByUserAndIsActive(User user, Boolean isActive);

    long countByUser(User user);

    List<SavingsGoal> findAllByUser(User currentUser);
}
