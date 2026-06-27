package ru.anokhin.dev.onlinegoodsstore.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDao(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stock,
        String imageUrl,
        boolean active,
        LocalDateTime createdAt,
        Long categoryId,
        String categoryName
) {
}
