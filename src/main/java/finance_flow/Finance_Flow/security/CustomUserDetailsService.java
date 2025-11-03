package finance_flow.Finance_Flow.security;

import finance_flow.Finance_Flow.model.User;
import finance_flow.Finance_Flow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Loading user by email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });

        log.debug("User loaded successfully: {}", email);

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        log.debug("Loading user by id: {}", id);

        User user = userRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> {
                    log.warn("User not found with id: {}", id);
                    return new UsernameNotFoundException("User not found with id: " + id);
                });

        log.debug("User loaded successfully by id: {}", id);

        return UserPrincipal.create(user);
    }
}
