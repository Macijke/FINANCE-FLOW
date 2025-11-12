package finance_flow.Finance_Flow.service.impl;

import finance_flow.Finance_Flow.dto.response.UserResponse;
import finance_flow.Finance_Flow.exception.ResourceNotFoundException;
import finance_flow.Finance_Flow.model.User;
import finance_flow.Finance_Flow.repository.UserRepository;
import finance_flow.Finance_Flow.service.UserService;
import finance_flow.Finance_Flow.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getUserProfile() {
        User currentUser = SecurityUtils.getCurrentUser();
        User user = userRepository.findById(Math.toIntExact(currentUser.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToUserResponse(user);
    }


    @Transactional
    @Override
    public UserResponse updateProfilePicture(String profilePictureUrl) {
        User currentUser = SecurityUtils.getCurrentUser();
        User user = userRepository.findById(Math.toIntExact(currentUser.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setProfilePictureUrl(profilePictureUrl);
        User updatedUser = userRepository.save(user);

        return mapToUserResponse(updatedUser);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .defaultCurrency(user.getDefaultCurrency())
                .profilePictureUrl(user.getProfilePictureUrl())
                .role(user.getRole().name())
                .build();
    }
}
