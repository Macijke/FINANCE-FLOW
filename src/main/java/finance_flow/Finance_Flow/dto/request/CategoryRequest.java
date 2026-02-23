package finance_flow.Finance_Flow.dto.request;

import finance_flow.Finance_Flow.model.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CategoryRequest(
        @NotBlank(message = "Category name is required")
        @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
        String name,
        @NotNull(message = "Transaction type is required")
        TransactionType type,
        @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Color must be a valid hex color code")
        String color,
        @Size(max = 50, message = "Icon must not exceed 50 characters")
        String icon,
        @Size(max = 255, message = "Description must not exceed 255 characters")
        String description,
        Integer displayOrder
) {
    public CategoryRequest {
        if (displayOrder == null) displayOrder = 0;
    }
}