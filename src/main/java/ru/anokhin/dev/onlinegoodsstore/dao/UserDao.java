package ru.anokhin.dev.onlinegoodsstore.dao;

import java.time.LocalDateTime;
import java.util.Set;

public record UserDao(
        Long id,
        String username,
        Long personId,
        Set<String> roles,
        LocalDateTime createAt,
        boolean isBlocked,
        LocalDateTime blockedAt
) {
}
