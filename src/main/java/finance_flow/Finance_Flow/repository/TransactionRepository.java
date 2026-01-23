package finance_flow.Finance_Flow.repository;

import finance_flow.Finance_Flow.model.Category;
import finance_flow.Finance_Flow.model.Transaction;
import finance_flow.Finance_Flow.model.User;
import finance_flow.Finance_Flow.model.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    Optional<Transaction> findByIdAndUser(Long id, User user);

    List<Transaction> findByUserAndType(User user, TransactionType type);

    @Query("SELECT t FROM Transaction t WHERE t.user = :user " +
            "AND (:categoryId IS NULL OR t.category.id = :categoryId) " +
            "AND (:type IS NULL OR t.type = :type) " +
            "AND (:startDate IS NULL OR t.transactionDate >= :startDate) " +
            "AND (:endDate IS NULL OR t.transactionDate <= :endDate) " +
            "ORDER BY t.transactionDate DESC, t.createdAt DESC")
    Page<Transaction> findByUserAndFilters(
            @Param("user") User user,
            @Param("categoryId") Long categoryId,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.type = :type " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal sumByUserAndTypeAndDateRange(
            @Param("userId") Long userId,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COUNT(t) FROM Transaction t " +
            "WHERE t.user = :user " +
            "AND t.type = :type " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate")
    Integer countByUserAndTypeAndTransactionDateBetween(
            User user,
            TransactionType type,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.type = :type " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal calculatePeriodSum(
            Long userId,
            TransactionType type,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.category.id = :id " +
            "AND t.type = :transactionType " +
            "AND t.transactionDate BETWEEN :startOfMonth AND :endOfMonth")
    BigDecimal sumByCategoryAndTypeAndDateRange(Long id, TransactionType transactionType, LocalDate startOfMonth, LocalDate endOfMonth);


    @Query("SELECT t FROM Transaction t WHERE t.user = :user " +
            "ORDER BY t.transactionDate DESC, t.createdAt DESC")
    List<Transaction> findRecentTransactions(@Param("user") User user, Pageable pageable);

    @Query("SELECT t.category.name, SUM(t.amount) " +
            "FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.type = 'EXPENSE' " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY t.category.name")
    List<Object[]> getCategoryBreakdown(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    void deleteByUserAndTransactionDateBefore(User user, LocalDate date);

    Page<Transaction> findAllByUser(User currentUser, Pageable pageable);

    List<Transaction> findByUserAndTransactionDateBetween(User user, LocalDate startDate, LocalDate endDate);

    List<Transaction> findByUserAndCategory(User currentUser, Category category);

    @Query("SELECT t.category.id, COALESCE(SUM(t.amount), 0) " +
            "FROM Transaction t " +
            "WHERE t.user.id = :userId " +
            "AND t.type = :type " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY t.category.id")
    List<Object[]> sumByCategoryAndDateRange(
            @Param("userId") Long userId,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT FUNCTION('TO_CHAR', t.transactionDate, 'YYYY-MM'), " +
            "t.type, COALESCE(SUM(t.amount), 0) " +
            "FROM Transaction t " +
            "WHERE t.user.id = :userId " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('TO_CHAR', t.transactionDate, 'YYYY-MM'), t.type " +
            "ORDER BY FUNCTION('TO_CHAR', t.transactionDate, 'YYYY-MM')")
    List<Object[]> getMonthlyTrends(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COUNT(t) FROM Transaction t " +
            "WHERE t.user.id = :userId " +
            "AND t.category.id = :categoryId " +
            "AND t.type = :type " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate")
    Long countByCategoryAndTypeAndDateRange(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount " +
            "WHEN t.type = 'EXPENSE' THEN -t.amount ELSE 0 END), 0) " +
            "FROM Transaction t " +
            "WHERE t.user = :user")
    BigDecimal calculateTotalBalance(User user);

}
