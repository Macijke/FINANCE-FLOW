package finance_flow.Finance_Flow.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfilePictureRequest(
        @NotBlank(message = "Profile picture URL cannot be blank")
        String profilePictureUrl
) {

}