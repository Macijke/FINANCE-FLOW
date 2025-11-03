package finance_flow.Finance_Flow.service.impl;

import finance_flow.Finance_Flow.dto.request.LoginRequest;
import finance_flow.Finance_Flow.dto.request.RegisterRequest;
import finance_flow.Finance_Flow.dto.response.AuthResponse;
import finance_flow.Finance_Flow.exception.BadRequestException;
import finance_flow.Finance_Flow.exception.UnauthorizedException;
import finance_flow.Finance_Flow.model.User;
import finance_flow.Finance_Flow.model.enums.Role;
import finance_flow.Finance_Flow.repository.UserRepository;
import finance_flow.Finance_Flow.security.JwtTokenProvider;
import finance_flow.Finance_Flow.security.UserPrincipal;
import finance_flow.Finance_Flow.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.error("Invalid password for email {}", request.getEmail());
            throw new UnauthorizedException("Invalid email or password");
        }

        if (!user.getIsActive()) {
            log.warn("Login failed: Account disabled - {}", request.getEmail());
            throw new UnauthorizedException("Account is disabled");
        }

        UserPrincipal userPrincipal = UserPrincipal.create(user);
        String token = jwtTokenProvider.generateToken(userPrincipal);
        log.info("User logged in successfully: {}", user.getEmail());
        return buildAuthResponse(user, token);
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: Email already in use - {}", request.getEmail());
            throw new BadRequestException("Email already in use");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .defaultCurrency(request.getDefaultCurrency() != null ? request.getDefaultCurrency() : "USD")
                .role(Role.USER)
                .isActive(true)
                .emailVerified(false)
                .build();
        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", user.getEmail());
        UserPrincipal userPrincipal = UserPrincipal.create(savedUser);
        String token = jwtTokenProvider.generateToken(userPrincipal);
        return buildAuthResponse(user, token);
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
