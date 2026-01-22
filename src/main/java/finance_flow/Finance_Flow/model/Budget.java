package finance_flow.Finance_Flow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "budgets", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_user_category_month",
                columnNames = {"user_id", "category_id", "month"}
        )},
        indexes = {
                @Index(name = "idx_user_month", columnList = "user_id, month"),

        })

public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;

    @Column(name = "limit_amount", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    @NotNull
    @PositiveOrZero
    private BigDecimal limitAmount = BigDecimal.ZERO;

    @Column(name = "month", nullable = false)
    @NotNull
    private LocalDate month;

    @Column(name = "alert_enabled")
    @Builder.Default
    private Boolean alertEnabled = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Budget budget)) return false;
        return id != null && id.equals(budget.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
