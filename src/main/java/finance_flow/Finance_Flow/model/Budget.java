package finance_flow.Finance_Flow.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
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
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;

    @Column(name = "limit_amount", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal limitAmount = BigDecimal.ZERO;

    @Column(name = "month", nullable = false)
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
}
