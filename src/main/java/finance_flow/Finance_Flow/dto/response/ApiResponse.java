package finance_flow.Finance_Flow.dto.response;

import lombok.Builder;

@Builder
public record ApiResponse<T>(
        boolean success,
        String message,
        Object data
) {

}