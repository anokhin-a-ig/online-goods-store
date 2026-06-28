package ru.anokhin.dev.onlinegoodsstore.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anokhin.dev.onlinegoodsstore.entity.User;
import ru.anokhin.dev.onlinegoodsstore.entity.enums.Role;
import ru.anokhin.dev.onlinegoodsstore.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String email, String password, String name, String deliveryAddress) {
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d).{8,}$")) {
            throw new IllegalArgumentException(
                    "Пароль должен содержать минимум 8 символов, хотя бы одну цифру и одну латинскую букву");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setDeliveryAddress(deliveryAddress);
        user.setRole(Role.ROLE_USER);
        user.setBlocked(false);
        user.setFailedAttempts(0);
        return userRepository.save(user);
    }

    public void onAuthenticationSuccess(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setFailedAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);
        });
    }

    public void onAuthenticationFailure(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setFailedAttempts(user.getFailedAttempts() + 1);
            if (user.getFailedAttempts() >= 5) {
                user.setLockTime(LocalDateTime.now().plusMinutes(15));
            }
            userRepository.save(user);
        });
    }
}
