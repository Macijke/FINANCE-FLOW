package finance_flow.Finance_Flow.dto.response;

import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        String defaultCurrency,
        String profilePictureUrl,
        String role
) {

}