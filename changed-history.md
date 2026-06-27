# История изменений

| Дата | Автор | Файл | Изменение |
|------|-------|------|-----------|
| 2026-06-28 | ai-agent | `dao/UserDao.java` | Исправлены поля в соответствии с entity User: заменены username/personId/isBlocked/blockedAt на email/password/name/deliveryAddress/role/blocked/failedAttempts/lockTime/createdAt |
| 2026-06-28 | ai-agent | `dao/CategoryDao.java` | Создан record для категории: id, name, description, parentId, parentName, active |
| 2026-06-28 | ai-agent | `dao/ProductDao.java` | Создан record для товара: id, name, description, price, stock, imageUrl, active, createdAt, categoryId, categoryName |
| 2026-06-28 | ai-agent | `dao/CartDao.java` | Создан record для корзины: id, userId, sessionId, createdAt, items |
| 2026-06-28 | ai-agent | `dao/CartItemDao.java` | Создан record для позиции корзины: id, cartId, productId, productName, quantity, productPrice |
| 2026-06-28 | ai-agent | `dao/OrderDao.java` | Создан record для заказа: id, userId, userName, orderDate, deliveryAddress, paymentMethod, status, totalSum, items |
| 2026-06-28 | ai-agent | `dao/OrderItemDao.java` | Создан record для позиции заказа: id, orderId, productId, productName, quantity, priceAtPurchase |
| 2026-06-28 | ai-agent | `dao/AuditLogDao.java` | Создан record для аудита: id, adminId, action, targetId, timestamp |
