package finance_flow.Finance_Flow.controller;

import finance_flow.Finance_Flow.dto.request.UpdateProfilePictureRequest;
import finance_flow.Finance_Flow.dto.response.ApiResponse;
import finance_flow.Finance_Flow.dto.response.UserResponse;
import finance_flow.Finance_Flow.security.UserPrincipal;
import finance_flow.Finance_Flow.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile() {
        UserResponse userResponse = userService.getUserProfile();

        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .success(true)
                .message("Profile retrieved successfully")
                .data(userResponse)
                .build());
    }

    @PutMapping("/profile-picture")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfilePicture(@Valid @RequestBody UpdateProfilePictureRequest request) {

        UserResponse userResponse = userService.updateProfilePicture(request.getProfilePictureUrl());

        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .success(true)
                .message("Profile picture updated successfully")
                .data(userResponse)
                .build());
    }
}
