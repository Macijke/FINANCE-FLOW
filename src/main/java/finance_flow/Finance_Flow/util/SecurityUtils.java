package finance_flow.Finance_Flow.util;

import com.sun.security.auth.UserPrincipal;
import finance_flow.Finance_Flow.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).getUser();
        }

        throw new UnauthorizedException("Invalid authentication principal");
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
