package ru.anokhin.dev.onlinegoodsstore.dao;

public record CategoryDao(
        Long id,
        String name,
        String description,
        Long parentId,
        String parentName,
        boolean active
) {
}
