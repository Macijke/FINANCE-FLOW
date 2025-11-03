package finance_flow.Finance_Flow.dto.response;

import finance_flow.Finance_Flow.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private Long id;
    private String categoryName;
    private String categoryColor;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private Long categoryId;
    private LocalDate transactionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
