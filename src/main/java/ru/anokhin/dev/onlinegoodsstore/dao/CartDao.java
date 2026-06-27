package ru.anokhin.dev.onlinegoodsstore.dao;

import java.time.LocalDateTime;
import java.util.List;

public record CartDao(
        Long id,
        Long userId,
        String sessionId,
        LocalDateTime createdAt,
        List<CartItemDao> items
) {
}
