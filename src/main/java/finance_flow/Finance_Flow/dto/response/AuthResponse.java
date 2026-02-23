package finance_flow.Finance_Flow.dto.response;

import lombok.Builder;

@Builder
public record AuthResponse(
        String token,
        String tokenType,
        Long userId,
        String email,
        String firstName,
        String lastName,
        String role
) {

}