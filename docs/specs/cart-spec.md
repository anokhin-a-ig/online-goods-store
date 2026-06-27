# Корзина

## Сущности

- `Cart` — `entity/Cart.java`
- `CartItem` — `entity/CartItem.java`

## Cart

| Поле | Тип | Ограничения |
|------|-----|-------------|
| id | Long | PK, автоинкремент |
| user | OneToOne → User | может быть null (гость) |
| sessionId | String | для гостей, если user = null |
| createdAt | LocalDateTime | |

Связи: `@OneToMany → CartItem (cascade ALL, orphanRemoval)`

## CartItem

| Поле | Тип | Ограничения |
|------|-----|-------------|
| id | Long | PK |
| cart | ManyToOne → Cart | not null |
| product | ManyToOne → Product | not null |
| quantity | int | >= 1 |

Уникальное ограничение: (cart_id, product_id)

## Функциональность

### Без авторизации (гость)
- Корзина привязана к sessionId (HTTP session)
- Добавление/удаление/изменение количества
- Хранится в БД с sessionId, без user

### После авторизации
- Перенос гостевой корзины к пользователю (по sessionId → user)
- Одна корзина на пользователя (merge если уже была)
- Все операции — в БД

### Операции
- Добавить товар (+1 или указать количество)
- Удалить позицию
- Изменить количество
- Очистить корзину
- Отобразить общую сумму

## DAO

- `CartDao` — id, userId, sessionId, createdAt, items (List<CartItemDao>)
- `CartItemDao` — id, cartId, productId, productName, quantity, productPrice
