package ru.anokhin.dev.onlinegoodsstore.dao;

import ru.anokhin.dev.onlinegoodsstore.entity.enums.Role;

import java.time.LocalDateTime;

public record UserDao(
        Long id,
        String email,
        String password,
        String name,
        String deliveryAddress,
        Role role,
        boolean blocked,
        int failedAttempts,
        LocalDateTime lockTime,
        LocalDateTime createdAt
) {
}
