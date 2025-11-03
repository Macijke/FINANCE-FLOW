package finance_flow.Finance_Flow.service;

import finance_flow.Finance_Flow.dto.request.TransactionRequest;
import finance_flow.Finance_Flow.dto.response.TransactionResponse;
import finance_flow.Finance_Flow.model.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    TransactionResponse createTransaction(TransactionRequest request);

    TransactionResponse updateTransaction(Long id, TransactionRequest request);

    void deleteTransaction(Long id);

    TransactionResponse getTransactionById(Long id);

    Page<TransactionResponse> getAllTransactions(Pageable pageable);

    List<TransactionResponse> getRecentTransactions(int limit);

    List<TransactionResponse> getTransactionsByType(TransactionType type);

    List<TransactionResponse> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate);

    List<TransactionResponse> getTransactionsByCategory(Long categoryId);

    BigDecimal getTotalByTypeAndDateRange(TransactionType type, LocalDate startDate, LocalDate endDate);
}
