package ru.anokhin.dev.onlinegoodsstore.dao;

import java.time.LocalDateTime;

public record AuditLogDao(
        Long id,
        Long adminId,
        String action,
        Long targetId,
        LocalDateTime timestamp
) {
}
