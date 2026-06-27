# API

## Базовые URL

- Контекст: `/online-goods-store`
- REST API: `/online-goods-store/api/**` (опционально)
- Основные эндпоинты — напрямую через MVC + Thymeleaf или REST

## Формат ответов

### Успех (200/201)
```json
{
  "data": { ... },
  "timestamp": "2026-06-27T12:00:00"
}
```

### Пагинация (`GET /products`, `GET /orders`)
```json
{
  "data": {
    "content": [...],
    "totalPages": 5,
    "totalElements": 48,
    "number": 0,
    "size": 10
  },
  "timestamp": "..."
}
```

### Ошибка (4xx/5xx)
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Товар с id=5 не найден",
  "path": "/products/5",
  "timestamp": "2026-06-27T12:00:00"
}
```

## Эндпоинты

### Публичные (ANY)
| Метод | URL | Описание |
|-------|-----|----------|
| GET | /products | список товаров (пагинированный, с фильтрами) |
| GET | /products/{id} | карточка товара |
| POST | /cart/add/{productId} | добавить в корзину |
| GET | /cart | просмотр корзины |
| POST | /cart/update | изменить количество |
| DELETE | /cart/remove/{itemId} | удалить из корзины |

### Зарегистрированные пользователи (USER)
| Метод | URL | Описание |
|-------|-----|----------|
| POST | /orders/create | оформить заказ |
| GET | /orders | история заказов (пагинированная) |
| POST | /orders/{id}/cancel | отменить заказ |

### Администратор (ADMIN)
| Метод | URL | Описание |
|-------|-----|----------|
| CRUD | /admin/categories | управление категориями |
| CRUD | /admin/products | управление товарами |
| GET | /admin/orders | все заказы с фильтром |
| PUT | /admin/orders/{id}/status | изменить статус заказа |
| GET | /admin/users | список пользователей |
| PUT | /admin/users/{id}/block | блокировка/разблокировка |

## Глобальный обработчик ошибок

`GlobalExceptionHandler` с `@RestControllerAdvice`:
- `EntityNotFoundException` → 404
- `InsufficientStockException` → 400
- `InvalidOrderStatusException` → 400
- `MethodArgumentNotValidException` → 400
- `AccessDeniedException` → 403
- `Exception` → 500
