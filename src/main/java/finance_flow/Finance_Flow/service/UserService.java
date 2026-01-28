package finance_flow.Finance_Flow.service;

import finance_flow.Finance_Flow.dto.response.UserResponse;

public interface UserService {
    UserResponse getUserProfile();
    UserResponse updateProfilePicture(String profilePictureUrl);

}
