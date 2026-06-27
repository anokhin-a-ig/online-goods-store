# Заказы

## Сущности

- `Order` — `entity/Order.java`
- `OrderItem` — `entity/OrderItem.java`
- `OrderStatus` — enum `CREATED, PAID, SHIPPED, DELIVERED, CANCELLED`
- `PaymentMethod` — enum `CARD, CASH`

## Order

| Поле | Тип | Ограничения |
|------|-----|-------------|
| id | Long | PK |
| user | ManyToOne → User | not null |
| orderDate | LocalDateTime | updatable false |
| deliveryAddress | String | копия из профиля на момент заказа |
| paymentMethod | PaymentMethod | |
| status | OrderStatus | not null |
| totalSum | BigDecimal | precision 10, scale 2 |
| items | OneToMany → OrderItem | cascade ALL, orphanRemoval |

Индексы: user_id, status

## OrderItem

| Поле | Тип | Ограничения |
|------|-----|-------------|
| id | Long | PK |
| order | ManyToOne → Order | not null |
| product | ManyToOne → Product | not null |
| quantity | int | >= 1 |
| priceAtPurchase | BigDecimal | цена на момент заказа, precision 10 scale 2 |

## Функциональность

### Оформление заказа
- Выбор адреса доставки (из профиля или новый)
- Выбор способа оплаты
- Транзакционное списание остатков (optimistic locking на Product.version)
- Проверка stock >= quantity для каждой позиции
- При конфликте версии — повтор транзакции или отказ
- Статус при создании: `CREATED`
- Фиксация цены в priceAtPurchase

### Статусы
- CREATED → PAID → SHIPPED → DELIVERED
- CREATED → CANCELLED (только пользователем, только из CREATED)
- PAID/SHIPPED/DELIVERED — только админ меняет

### История заказов
- Для пользователя: пагинированный список своих заказов
- Для админа: все заказы с фильтром по статусу и пользователю

### Отмена заказа
- Пользователь отменяет только в статусе CREATED
- Возврат остатков на склад при отмене

## DAO

- `OrderDao` — id, userId, userName, orderDate, deliveryAddress, paymentMethod, status, totalSum, items
- `OrderItemDao` — id, orderId, productId, productName, quantity, priceAtPurchase
