package finance_flow.Finance_Flow.model;

import finance_flow.Finance_Flow.model.enums.GoalStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "savings_goals")
@Data
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
    private String name;

    @Column(length = 500, name = "description")
    private String description;

    @Column(nullable = false, name = "target_amount")
    private BigDecimal targetAmount;

    @Column(nullable = false, name = "current_amount")
    private BigDecimal currentAmount;

    @Column(nullable = false, name = "target_date")
    private LocalDate targetDate;

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private GoalStatus status;

    @Column(nullable = false, name = "icon")
    private String icon;

    @Column(nullable = false, name = "color")
    private String color;

    @Column(nullable = false, name = "is_active")
    private Boolean isActive;

    @CreatedDate
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;
}
