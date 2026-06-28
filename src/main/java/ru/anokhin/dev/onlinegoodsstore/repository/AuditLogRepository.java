package ru.anokhin.dev.onlinegoodsstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.anokhin.dev.onlinegoodsstore.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
