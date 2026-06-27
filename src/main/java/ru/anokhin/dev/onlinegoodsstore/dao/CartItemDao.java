package ru.anokhin.dev.onlinegoodsstore.dao;

public record CartItemDao(
        Long id,
        Long cartId,
        Long productId,
        String productName,
        int quantity,
        java.math.BigDecimal productPrice
) {
}
