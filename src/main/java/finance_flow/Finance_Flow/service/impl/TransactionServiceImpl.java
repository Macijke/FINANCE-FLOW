package finance_flow.Finance_Flow.service.impl;

import finance_flow.Finance_Flow.dto.request.TransactionRequest;
import finance_flow.Finance_Flow.dto.response.TransactionResponse;
import finance_flow.Finance_Flow.exception.ResourceNotFoundException;
import finance_flow.Finance_Flow.model.Category;
import finance_flow.Finance_Flow.model.Transaction;
import finance_flow.Finance_Flow.model.User;
import finance_flow.Finance_Flow.model.enums.TransactionType;
import finance_flow.Finance_Flow.repository.CategoryRepository;
import finance_flow.Finance_Flow.repository.TransactionRepository;
import finance_flow.Finance_Flow.service.TransactionService;
import finance_flow.Finance_Flow.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {
        log.info("Creating new transaction: {}", request);

        User currentUser = SecurityUtils.getCurrentUser();

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findByIdAndUser(request.getCategoryId(), currentUser)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        }

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .description(request.getDescription())
                .transactionDate(request.getTransactionDate())
                .user(currentUser)
                .category(category)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        log.info("Transaction created successfully with id: {}", savedTransaction.getId());

        return mapToResponse(savedTransaction);
    }


    @Override
    @Transactional
    public TransactionResponse updateTransaction(Long id, TransactionRequest request) {
        log.info("Updating transaction: {}", request);
        User currentUser = SecurityUtils.getCurrentUser();
        Transaction transaction = transactionRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDescription(request.getDescription());
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToResponse(updatedTransaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        User currentUser = SecurityUtils.getCurrentUser();
        Transaction transaction = transactionRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        transactionRepository.delete(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Long id) {
        User currentUser = SecurityUtils.getCurrentUser();
        Transaction transaction = transactionRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        return mapToResponse(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponse> getAllTransactions(Pageable pageable) {
        User currentUser = SecurityUtils.getCurrentUser();
        Page<Transaction> transactions = transactionRepository.findAllByUser(currentUser, pageable);
        return transactions.map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getRecentTransactions(int limit) {
        User currentUser = SecurityUtils.getCurrentUser();
        List<Transaction> transactions = transactionRepository.findRecentTransactions(currentUser, Pageable.ofSize(limit));

        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByType(TransactionType type) {
        User currentUser = SecurityUtils.getCurrentUser();
        List<Transaction> transactions = transactionRepository.findByUserAndType(currentUser, type);

        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        User currentUser = SecurityUtils.getCurrentUser();
        List<Transaction> transactions = transactionRepository.findByUserAndTransactionDateBetween(currentUser, startDate, endDate);

        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByCategory(Long categoryId) {
        User currentUser = SecurityUtils.getCurrentUser();
        Category category = categoryRepository.findById(Math.toIntExact(categoryId))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        List<Transaction> transactions = transactionRepository.findByUserAndCategory(currentUser, category);
        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalByTypeAndDateRange(TransactionType type, LocalDate startDate, LocalDate endDate) {
        User currentUser = SecurityUtils.getCurrentUser();
        return transactionRepository.sumByUserAndTypeAndDateRange(currentUser.getId(), type, startDate, endDate);
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .categoryId(transaction.getCategory() != null ? transaction.getCategory().getId() : null)
                .categoryName(transaction.getCategory() != null ? transaction.getCategory().getName() : null)
                .categoryIcon(transaction.getCategory().getIcon() != null ? transaction.getCategory().getIcon() : null)
                .categoryColor(transaction.getCategory() != null ? transaction.getCategory().getColor() : null)
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }

}
