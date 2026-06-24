package ru.anokhin.dev.onlinegoodsstore.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.anokhin.dev.onlinegoodsstore.dao.RegistrationDao;
import ru.anokhin.dev.onlinegoodsstore.entity.Role;
import ru.anokhin.dev.onlinegoodsstore.entity.User;
import ru.anokhin.dev.onlinegoodsstore.exception.UserAlreadyExistsException;
import ru.anokhin.dev.onlinegoodsstore.repository.RoleRepository;
import ru.anokhin.dev.onlinegoodsstore.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class UserService {

    private static final String DEFAULT_ROLE = "ROLE_USER";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public String saveNewUser(RegistrationDao dao) {
        if (userRepository.findUserByUsername(dao.username()).isPresent()) {
            throw new UserAlreadyExistsException("User with username '" + dao.username() + "' already exists");
        }

        Role defaultRole = roleRepository.findByRoleName(DEFAULT_ROLE)
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = new User();
        LocalDateTime time = LocalDateTime.now();
        user.setUsername(dao.username().trim());
        user.setPassword(passwordEncoder.encode(dao.password()));
        user.setCreateAt(time);
        user.setBlocked(false);
        user.setRoles(Set.of(defaultRole));
        userRepository.save(user);
        return dao.username().trim();
    }
}
