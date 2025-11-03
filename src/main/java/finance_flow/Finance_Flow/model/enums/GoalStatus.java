package finance_flow.Finance_Flow.model.enums;

import lombok.Getter;

@Getter
public enum GoalStatus {
    ACTIVE("Active"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    GoalStatus(String displayName) {
        this.displayName = displayName;
    }

}
