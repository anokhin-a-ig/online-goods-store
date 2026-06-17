package ru.anokhin.dev.onlinegoodsstore.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.anokhin.dev.onlinegoodsstore.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("""
            UPDATE Users u
            SET u.username = :#{#newUser.username},
            u.password = :#{#newUser.password}
            WHERE u.id = :id
            """)
    void update(Long id, User newUser);

    Optional<User> findUserByUsername(String username);
}
