package finance_flow.Finance_Flow.repository;

import finance_flow.Finance_Flow.model.Category;
import finance_flow.Finance_Flow.model.User;
import finance_flow.Finance_Flow.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByUser(User user);
    Optional<Category> findByIdAndUser(Long id, User user);
    List<Category> findByUserAndType(User user, TransactionType type);
    boolean existsByUserAndNameAndType(User user, String name, TransactionType type);
    Optional<Category> findByUserAndNameAndType(User user, String name, TransactionType type);
    List<Category> findByIsDefaultTrue();
    long countByUser(User user);
    long countByUserAndType(User user, TransactionType type);

    @Query("SELECT c, COUNT(t) FROM Category c " +
            "LEFT JOIN c.transactions t " +
            "WHERE c.user = :user " +
            "GROUP BY c " +
            "ORDER BY c.displayOrder ASC")
    List<Object[]> findCategoriesWithTransactionCount(@Param("user") User user);
}
