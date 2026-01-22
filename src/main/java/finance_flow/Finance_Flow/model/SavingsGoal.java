package finance_flow.Finance_Flow.model;

import finance_flow.Finance_Flow.model.enums.GoalStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "savings_goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SavingsGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, name = "name")
    @NotBlank
    @Size(max = 100)
    private String name;

    @Column(length = 500, name = "description")
    @Size(max = 500)
    @NotNull
    private String description;

    @Column(nullable = false, name = "target_amount")
    @Positive
    private BigDecimal targetAmount;

    @Column(nullable = false, name = "current_amount")
    @PositiveOrZero
    private BigDecimal currentAmount;

    @Column(nullable = false, name = "target_date")
    @Future
    private LocalDate targetDate;

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private GoalStatus status;

    @Column(nullable = false, name = "icon")
    private String icon;

    @Column(nullable = false, name = "color")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Color must be a valid hex code")
    private String color;

    @Column(nullable = false, name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreatedDate
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SavingsGoal savingsGoal)) return false;
        return id != null && id.equals(savingsGoal.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
