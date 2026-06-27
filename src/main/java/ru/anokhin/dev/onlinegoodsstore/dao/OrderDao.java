package ru.anokhin.dev.onlinegoodsstore.dao;

import ru.anokhin.dev.onlinegoodsstore.entity.enums.OrderStatus;
import ru.anokhin.dev.onlinegoodsstore.entity.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDao(
        Long id,
        Long userId,
        String userName,
        LocalDateTime orderDate,
        String deliveryAddress,
        PaymentMethod paymentMethod,
        OrderStatus status,
        BigDecimal totalSum,
        List<OrderItemDao> items
) {
}
