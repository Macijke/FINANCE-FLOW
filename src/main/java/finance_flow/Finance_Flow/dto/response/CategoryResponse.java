package finance_flow.Finance_Flow.dto.response;

import finance_flow.Finance_Flow.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Long id;
    private String name;
    private TransactionType type;
    private String color;
    private String icon;
    private String description;
    private Boolean isDefault;
    private Boolean isActive;
    private Integer displayOrder;
    private LocalDateTime createdAt;
}
