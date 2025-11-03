package finance_flow.Finance_Flow.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingsGoalRequest {

    @NotBlank(message = "Goal name is required")
    @Size(min = 2, max = 100, message = "Goal name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Target amount is required")
    @DecimalMin(value = "0.01", message = "Target amount must be greater than 0")
    private BigDecimal targetAmount;

    @NotNull(message = "Target date is required")
    @FutureOrPresent(message = "Target date must be today or in the future")
    private LocalDate targetDate;

    @Size(max = 10, message = "Icon must not exceed 10 characters")
    private String icon;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Color must be a valid hex color code")
    private String color;
}
