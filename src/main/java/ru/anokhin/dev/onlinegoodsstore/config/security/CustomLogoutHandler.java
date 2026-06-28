package ru.anokhin.dev.onlinegoodsstore.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import ru.anokhin.dev.onlinegoodsstore.entity.AuditLog;
import ru.anokhin.dev.onlinegoodsstore.entity.User;
import ru.anokhin.dev.onlinegoodsstore.repository.AuditLogRepository;
import ru.anokhin.dev.onlinegoodsstore.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
        if (authentication != null) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin) {
                String email = authentication.getName();
                User user = userRepository.findByEmail(email).orElse(null);
                if (user != null) {
                    AuditLog log = new AuditLog();
                    log.setAdminId(user.getId());
                    log.setAction("LOGOUT");
                    auditLogRepository.save(log);
                }
            }
        }
    }
}
