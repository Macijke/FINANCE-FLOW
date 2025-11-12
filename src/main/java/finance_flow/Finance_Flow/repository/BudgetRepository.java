package finance_flow.Finance_Flow.repository;

import finance_flow.Finance_Flow.model.Budget;
import finance_flow.Finance_Flow.model.Category;
import finance_flow.Finance_Flow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByIdAndUser(Long id, User user);

    List<Budget> findByUser(User user);

    List<Budget> findByUserAndMonth(User user, LocalDate month);

    Optional<Budget> findByUserAndCategoryAndMonth(User user, Category category, LocalDate month);

    boolean existsByUserAndCategoryAndMonth(User user, Category category, LocalDate month);

    long countByUser(User user);

    @Query("SELECT b FROM Budget b WHERE b.month = :month AND b.user = :user")
    List<Budget> findActiveBudgetsForMonth(User user, LocalDate month);

}
