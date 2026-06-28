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
| 2026-06-28 | ai-agent | `repository/UserRepository.java` | Создан репозиторий с методом findByEmail |
| 2026-06-28 | ai-agent | `repository/AuditLogRepository.java` | Создан JPA репозиторий для AuditLog |
| 2026-06-28 | ai-agent | `config/security/SecurityBeansConfig.java` | Создан конфиг с Bean PasswordEncoder (BCrypt) |
| 2026-06-28 | ai-agent | `config/security/SecurityConfig.java` | Создан основной конфиг безопасности: form login, logout, session, CSRF, rate limiter |
| 2026-06-28 | ai-agent | `config/security/CustomLogoutHandler.java` | Создан LogoutHandler для аудита выхода ADMIN |
| 2026-06-28 | ai-agent | `config/security/RateLimitFilter.java` | Создан OncePerRequestFilter (10 попыток/мин на /auth/login) |
| 2026-06-28 | ai-agent | `config/WebConfig.java` | Создан конфиг CORS (localhost:8080) |
| 2026-06-28 | ai-agent | `service/security/CustomUserDetailsService.java` | Создан UserDetailsService с проверкой blocked/lockTime |
| 2026-06-28 | ai-agent | `service/security/AuthenticationService.java` | Создан сервис регистрации, сброса и инкремента блокировок |
| 2026-06-28 | ai-agent | `controllers/mvc/AuthController.java` | Создан контроллер /auth/login и /auth/register |
| 2026-06-28 | ai-agent | `dao/RegistrationRequest.java` | Создан record с валидацией для формы регистрации |
| 2026-06-28 | ai-agent | `application-dev.yml` | Удалён блок spring.security.user.* (теперь аутентификация через БД) |
