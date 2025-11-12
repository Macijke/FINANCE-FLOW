package finance_flow.Finance_Flow.service;

import finance_flow.Finance_Flow.dto.response.UserResponse;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    UserResponse getUserProfile();
    UserResponse updateProfilePicture(String profilePictureUrl);

}
