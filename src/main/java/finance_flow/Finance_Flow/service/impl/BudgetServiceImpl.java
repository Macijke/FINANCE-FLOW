package finance_flow.Finance_Flow.service.impl;

import finance_flow.Finance_Flow.dto.request.BudgetRequest;
import finance_flow.Finance_Flow.dto.response.BudgetResponse;
import finance_flow.Finance_Flow.exception.BadRequestException;
import finance_flow.Finance_Flow.exception.ResourceNotFoundException;
import finance_flow.Finance_Flow.model.Budget;
import finance_flow.Finance_Flow.model.Category;
import finance_flow.Finance_Flow.model.User;
import finance_flow.Finance_Flow.model.enums.TransactionType;
import finance_flow.Finance_Flow.repository.BudgetRepository;
import finance_flow.Finance_Flow.repository.CategoryRepository;
import finance_flow.Finance_Flow.repository.TransactionRepository;
import finance_flow.Finance_Flow.service.BudgetService;
import finance_flow.Finance_Flow.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static finance_flow.Finance_Flow.model.enums.TransactionType.EXPENSE;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public BudgetResponse createBudget(BudgetRequest request) {
        log.info("Creating new budget: {}", request);

        User currentUser = SecurityUtils.getCurrentUser();

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findByIdAndUser(request.getCategoryId(), currentUser)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        }

        if (budgetRepository.existsByUserAndCategoryAndMonth(currentUser, category, request.getMonth())) {
            throw new BadRequestException("Budget already exists for this category and month");
        }

        Budget budget = Budget.builder()
                .user(currentUser)
                .category(category)
                .limitAmount(request.getLimitAmount())
                .month(request.getMonth())
                .alertEnabled(request.getAlertEnabled() != null ? request.getAlertEnabled() : true)
                .build();

        Budget savedBudget = budgetRepository.save(budget);
        log.info("Budget created successfully with id: {}", savedBudget.getId());

        return mapToResponse(savedBudget);
    }


    @Override
    @Transactional
    public BudgetResponse updateBudget(Long id, BudgetRequest request) {
        log.info("Updating budget with id: {}", id);

        User currentUser = SecurityUtils.getCurrentUser();
        Budget budget = budgetRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findByIdAndUser(request.getCategoryId(), currentUser)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        }

        if (!Objects.equals(budget.getCategory(), category) ||
                !Objects.equals(budget.getMonth(), request.getMonth())) {

            boolean exists = budgetRepository.existsByUserAndCategoryAndMonth(
                    currentUser, category, request.getMonth());

            if (exists) {
                throw new BadRequestException("Budget already exists for this category and month");
            }
        }

        budget.setMonth(request.getMonth());
        budget.setLimitAmount(request.getLimitAmount());
        budget.setAlertEnabled(request.getAlertEnabled());
        budget.setCategory(category);

        Budget updatedBudget = budgetRepository.save(budget);
        log.info("Budget updated successfully");

        return mapToResponse(updatedBudget);
    }


    @Override
    @Transactional
    public void deleteBudget(Long id) {
        User currentUser = SecurityUtils.getCurrentUser();
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not exists"));
        if (!Objects.equals(budget.getUser().getId(), currentUser.getId())) {
            throw new ResourceNotFoundException("Budget not exists");
        }
        budgetRepository.delete(budget);
    }

    @Override
    @Transactional(readOnly = true)
    public BudgetResponse getBudgetById(Long id) {
        User currentUser = SecurityUtils.getCurrentUser();
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not exists"));
        if (!Objects.equals(budget.getUser().getId(), currentUser.getId())) {
            throw new ResourceNotFoundException("Budget not exists");
        }

        return mapToResponse(budget);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BudgetResponse> getAllBudgets() {
        User currentUser = SecurityUtils.getCurrentUser();
        List<Budget> budgetList = budgetRepository.findByUser(currentUser);

        return budgetList.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BudgetResponse> getBudgetsByMonth(LocalDate month) {
        User currentUser = SecurityUtils.getCurrentUser();
        List<Budget> budgetList = budgetRepository.findByUserAndMonth(currentUser, month);

        return budgetList.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BudgetResponse checkBudgetStatus(Long id) {
        User currentUser = SecurityUtils.getCurrentUser();
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not exists"));
        BudgetResponse response = mapToResponse(budget);

        if (response.getSpentAmount().compareTo(response.getLimitAmount()) > 0) {
            log.warn("Budget {} exceeded! Spent: {}, Limit: {}",
                    id, response.getSpentAmount(), response.getLimitAmount());
        }

        if (response.getPercentageUsed() >= 90 && response.getPercentageUsed() < 100) {
            log.info("Budget {} is almost reached ({}%)", id, response.getPercentageUsed());
        }

        return response;
    }

    private BudgetResponse mapToResponse(Budget budget) {
        BigDecimal spent = calculateSpentAmount(
                budget.getUser(),
                budget.getCategory(),
                budget.getMonth()
        );

        BigDecimal remaining = budget.getLimitAmount().subtract(spent);

        Integer percentageUsed = budget.getLimitAmount().compareTo(BigDecimal.ZERO) == 0
                ? 0
                : spent.multiply(BigDecimal.valueOf(100))
                .divide(budget.getLimitAmount(), 0, RoundingMode.HALF_UP)
                .intValue();

        return BudgetResponse.builder()
                .id(budget.getId())
                .categoryId(budget.getCategory().getId())
                .categoryName(budget.getCategory() != null
                        ? budget.getCategory().getName()
                        : "Global Budget")
                .categoryColor(budget.getCategory() != null
                        ? budget.getCategory().getColor()
                        : "#6B7280")
                .categoryIcon(budget.getCategory().getIcon())
                .limitAmount(budget.getLimitAmount())
                .month(budget.getMonth())
                .alertEnabled(budget.getAlertEnabled())
                .spentAmount(spent)
                .remainingAmount(remaining)
                .percentageUsed(percentageUsed)
                .createdAt(budget.getCreatedAt())
                .updatedAt(budget.getUpdatedAt())
                .build();
    }


    private BigDecimal calculateSpentAmount(User user, Category category, LocalDate month) {
        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = month.withDayOfMonth(month.lengthOfMonth());
        if (category == null) {
            return transactionRepository.sumByUserAndTypeAndDateRange(
                    user.getId(),
                    TransactionType.EXPENSE,
                    startOfMonth,
                    endOfMonth
            );
        }
        return transactionRepository.sumByCategoryAndTypeAndDateRange(
                category.getId(),
                EXPENSE,
                startOfMonth,
                endOfMonth
        );
    }
}

