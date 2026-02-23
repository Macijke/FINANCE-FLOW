package finance_flow.Finance_Flow.service.impl;

import finance_flow.Finance_Flow.dto.request.ChangePasswordRequest;
import finance_flow.Finance_Flow.dto.request.LoginRequest;
import finance_flow.Finance_Flow.dto.request.RegisterRequest;
import finance_flow.Finance_Flow.dto.response.AuthResponse;
import finance_flow.Finance_Flow.exception.BadRequestException;
import finance_flow.Finance_Flow.exception.ResourceNotFoundException;
import finance_flow.Finance_Flow.exception.UnauthorizedException;
import finance_flow.Finance_Flow.model.User;
import finance_flow.Finance_Flow.model.enums.Role;
import finance_flow.Finance_Flow.repository.UserRepository;
import finance_flow.Finance_Flow.security.JwtTokenProvider;
import finance_flow.Finance_Flow.security.UserPrincipal;
import finance_flow.Finance_Flow.service.AuthService;
import finance_flow.Finance_Flow.service.CategoryService;
import finance_flow.Finance_Flow.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        if (!user.getIsActive()) {
            throw new UnauthorizedException("Account is disabled");
        }

        UserPrincipal userPrincipal = UserPrincipal.create(user);
        String token = jwtTokenProvider.generateToken(userPrincipal);

        userRepository.updateLastLogin(user.getId(), LocalDateTime.now());
        log.info("User logged in successfully: {}", user.getEmail());
        return buildAuthResponse(user, token);
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            log.warn("Registration failed: Email already in use - {}", request.email());
            throw new BadRequestException("Email already in use");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .defaultCurrency(request.defaultCurrency())
                .role(Role.USER)
                .isActive(true)
                .emailVerified(false)
                .build();
        User savedUser = userRepository.save(user);
        UserPrincipal userPrincipal = UserPrincipal.create(savedUser);
        String token = jwtTokenProvider.generateToken(userPrincipal);
        categoryService.initializeDefaultCategories(userPrincipal);
        log.info("User registered successfully: {}", savedUser.getEmail());
        return buildAuthResponse(savedUser, token);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = SecurityUtils.getCurrentUser();

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            log.warn("Change password failed: Incorrect current password for user {}", user.getEmail());
            throw new UnauthorizedException("Current password is incorrect");
        }
        if (!request.newPassword().equals(request.confirmNewPassword())) {
            log.warn("Change password failed: New password and confirmation do not match for user {}", user.getEmail());
            throw new BadRequestException("New password and confirmation do not match");
        }

        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            log.warn("Change password failed: New password cannot be the same as the current password for user {}", user.getEmail());
            throw new BadRequestException("New password cannot be the same as the current password");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setPasswordChangedAt(LocalDateTime.now());
        userRepository.save(user);
        log.info("Password changed successfully for user {}", user.getEmail());
    }


    private AuthResponse buildAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .token(token)
                .tokenType("Bearer")
                .build();
    }
}
