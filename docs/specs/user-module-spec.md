# Модуль пользователей

## Сущности

- `User` — `entity/User.java`
- `Role` — `enum ROLE_USER, ROLE_ADMIN`

## Поля User

| Поле | Тип | Ограничения |
|------|-----|-------------|
| id | Long | PK, автоинкремент |
| email | String | unique, not null |
| password | String | BCrypt хеш, not null |
| name | String | not null |
| deliveryAddress | String | |
| role | Role (Enum String) | not null |
| blocked | boolean | default false |
| failedAttempts | int | default 0, сброс при успешном входе |
| lockTime | LocalDateTime | временная блокировка до, null — не заблокирован |
| createdAt | LocalDateTime | заполняется при persist |

## Связи

- `@OneToOne` → Cart (одна корзина на пользователя)
- `@OneToMany` → Order (история заказов)

## Функциональность

### Регистрация
- email + password + name + deliveryAddress
- password — BCrypt, минимум 8 символов, цифра + буква
- role по умолчанию `ROLE_USER`
- Проверка уникальности email

### Вход/Выход
- Spring Security Form Login
- После 5 неудачных попыток — блокировка на 15 мин (failedAttempts + lockTime)
- Сброс failedAttempts при успешном входе

### Личный кабинет
- Просмотр/редактирование профиля
- История заказов (пагинированная)

### Администрирование
- Блокировка/разблокировка пользователей (поле blocked)
- Смена роли (USER → ADMIN)

## DAO

`UserDao` — id, email, password, name, deliveryAddress, role, blocked, failedAttempts, lockTime, createdAt
