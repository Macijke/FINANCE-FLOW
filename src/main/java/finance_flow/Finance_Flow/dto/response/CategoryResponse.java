package finance_flow.Finance_Flow.dto.response;

import finance_flow.Finance_Flow.model.enums.TransactionType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CategoryResponse(
        Long id,
        String name,
        TransactionType type,
        String color,
        String icon,
        String description,
        Boolean isDefault,
        Boolean isActive,
        Integer displayOrder,
        LocalDateTime createdAt
) {

}