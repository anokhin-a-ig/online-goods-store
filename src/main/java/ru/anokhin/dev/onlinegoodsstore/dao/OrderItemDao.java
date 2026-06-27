package ru.anokhin.dev.onlinegoodsstore.dao;

import java.math.BigDecimal;

public record OrderItemDao(
        Long id,
        Long orderId,
        Long productId,
        String productName,
        int quantity,
        BigDecimal priceAtPurchase
) {
}
