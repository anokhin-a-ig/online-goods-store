package ru.anokhin.dev.onlinegoodsstore.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.anokhin.dev.onlinegoodsstore.dao.RegistrationDao;
import ru.anokhin.dev.onlinegoodsstore.entity.Person;
import ru.anokhin.dev.onlinegoodsstore.entity.User;
import ru.anokhin.dev.onlinegoodsstore.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.repo = repo;
    }

    @Transactional()
    public String saveNewUser(RegistrationDao dao) {
        if (repo.findUserByUsername(dao.username()).isPresent()) {
            return "User with username : " + dao.username() + " already exists";
        } else {
            User user = new User();
            LocalDateTime time = LocalDateTime.now();
            user.setUsername(dao.username());
            user.setPassword(passwordEncoder.encode(dao.password()));
            user.setCreateAt(time);
            user.setBlocked(false);
            user.setBlockedAt(null);
            user.setPerson(null);
            repo.save(user);
            return dao.username();
        }
    }
}
