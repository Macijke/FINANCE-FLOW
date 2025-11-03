package finance_flow.Finance_Flow.service.impl;

import finance_flow.Finance_Flow.dto.request.SavingsGoalRequest;
import finance_flow.Finance_Flow.dto.response.SavingsGoalResponse;
import finance_flow.Finance_Flow.exception.ResourceNotFoundException;
import finance_flow.Finance_Flow.model.SavingsGoal;
import finance_flow.Finance_Flow.model.User;
import finance_flow.Finance_Flow.model.enums.GoalStatus;
import finance_flow.Finance_Flow.repository.SavingsGoalRepository;
import finance_flow.Finance_Flow.service.SavingsGoalService;
import finance_flow.Finance_Flow.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SavingGoalServiceImpl implements SavingsGoalService {

    private final SavingsGoalRepository savingsGoalRepository;

    @Override
    @Transactional
    public SavingsGoalResponse createGoal(SavingsGoalRequest request) {
        log.info("Creating new savings goal: {}", request.getName());

        User currentUser = SecurityUtils.getCurrentUser();

        SavingsGoal savingsGoal = SavingsGoal.builder()
                .name(request.getName())
                .description(request.getDescription())
                .targetAmount(request.getTargetAmount())
                .currentAmount(BigDecimal.ZERO)
                .targetDate(request.getTargetDate())
                .status(GoalStatus.ACTIVE)
                .icon(request.getIcon() != null ? request.getIcon() : "🎯")
                .color(request.getColor() != null ? request.getColor() : "#3B82F6")
                .isActive(true)
                .user(currentUser)
                .build();

        SavingsGoal savedGoal = savingsGoalRepository.save(savingsGoal);
        log.info("Savings goal created successfully with id: {}", savedGoal.getId());

        return mapToResponse(savedGoal);
    }

    @Override
    @Transactional
    public SavingsGoalResponse updateGoal(Long id, SavingsGoalRequest request) {
        log.info("Updating savings goal with id: {}", id);

        User currentUser = SecurityUtils.getCurrentUser();

        SavingsGoal savingsGoal = savingsGoalRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Savings Goal not found"));

        savingsGoal.setName(request.getName());
        savingsGoal.setDescription(request.getDescription());
        savingsGoal.setTargetAmount(request.getTargetAmount());
        savingsGoal.setTargetDate(request.getTargetDate());

        if (request.getIcon() != null) {
            savingsGoal.setIcon(request.getIcon());
        }
        if (request.getColor() != null) {
            savingsGoal.setColor(request.getColor());
        }

        SavingsGoal updatedGoal = savingsGoalRepository.save(savingsGoal);
        log.info("Savings goal updated successfully");

        return mapToResponse(updatedGoal);
    }

    @Override
    @Transactional
    public void deleteGoal(Long id) {
        log.info("Deleting savings goal with id: {}", id);

        User currentUser = SecurityUtils.getCurrentUser();

        SavingsGoal savingsGoal = savingsGoalRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Savings Goal not found"));

        savingsGoalRepository.delete(savingsGoal);
        log.info("Savings goal deleted successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public SavingsGoalResponse getGoalById(Long id) {
        User currentUser = SecurityUtils.getCurrentUser();

        SavingsGoal savingsGoal = savingsGoalRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Savings Goal not found"));

        return mapToResponse(savingsGoal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SavingsGoalResponse> getAllGoals() {
        User currentUser = SecurityUtils.getCurrentUser();

        List<SavingsGoal> goals = savingsGoalRepository.findByUser(currentUser);

        return goals.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SavingsGoalResponse> getGoalsByStatus(GoalStatus status) {
        User currentUser = SecurityUtils.getCurrentUser();

        List<SavingsGoal> goals = savingsGoalRepository.findByUserAndStatus(currentUser, status);

        return goals.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public SavingsGoalResponse contributeToGoal(Long id, BigDecimal amount) {
        log.info("Contributing {} to goal {}", amount, id);

        User currentUser = SecurityUtils.getCurrentUser();

        SavingsGoal savingsGoal = savingsGoalRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Savings Goal not found"));

        BigDecimal newAmount = savingsGoal.getCurrentAmount().add(amount);
        savingsGoal.setCurrentAmount(newAmount);

        SavingsGoal updatedGoal = savingsGoalRepository.save(savingsGoal);
        log.info("Contribution added successfully");

        return mapToResponse(updatedGoal);
    }

    @Override
    @Transactional
    public SavingsGoalResponse completeGoal(Long id) {
        log.info("Completing savings goal with id: {}", id);

        User currentUser = SecurityUtils.getCurrentUser();

        SavingsGoal savingsGoal = savingsGoalRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Savings Goal not found"));

        savingsGoal.setStatus(GoalStatus.COMPLETED);

        SavingsGoal updatedGoal = savingsGoalRepository.save(savingsGoal);
        log.info("Savings goal completed successfully");

        return mapToResponse(updatedGoal);
    }

    private SavingsGoalResponse mapToResponse(SavingsGoal savingsGoal) {
        BigDecimal remaining = savingsGoal.getTargetAmount()
                .subtract(savingsGoal.getCurrentAmount());

        Integer percentageCompleted = savingsGoal.getTargetAmount()
                .compareTo(BigDecimal.ZERO) == 0
                ? 0
                : savingsGoal.getCurrentAmount()
                .multiply(BigDecimal.valueOf(100))
                .divide(savingsGoal.getTargetAmount(), 0, RoundingMode.HALF_UP)
                .intValue();

        return SavingsGoalResponse.builder()
                .id(savingsGoal.getId())
                .name(savingsGoal.getName())
                .description(savingsGoal.getDescription())
                .targetAmount(savingsGoal.getTargetAmount())
                .currentAmount(savingsGoal.getCurrentAmount())
                .remainingAmount(remaining)
                .percentageCompleted(percentageCompleted)
                .targetDate(savingsGoal.getTargetDate())
                .status(savingsGoal.getStatus())
                .icon(savingsGoal.getIcon())
                .color(savingsGoal.getColor())
                .isActive(savingsGoal.getIsActive())
                .createdAt(savingsGoal.getCreatedAt())
                .updatedAt(savingsGoal.getUpdatedAt())
                .build();
    }
}
