# База данных

## Таблицы

### users
| Поле | Тип | Ограничения |
|------|-----|-------------|
| id | BIGSERIAL | PK |
| email | VARCHAR(255) | UNIQUE, NOT NULL |
| password | VARCHAR(255) | NOT NULL |
| name | VARCHAR(255) | NOT NULL |
| delivery_address | VARCHAR(255) | |
| role | VARCHAR(20) | NOT NULL |
| blocked | BOOLEAN | DEFAULT false |
| failed_attempts | INTEGER | DEFAULT 0 |
| lock_time | TIMESTAMP | |
| created_at | TIMESTAMP | NOT NULL |

### categories
| Поле | Тип | Ограничения |
|------|-----|-------------|
| id | BIGSERIAL | PK |
| name | VARCHAR(255) | UNIQUE, NOT NULL |
| description | VARCHAR(255) | |
| parent_id | BIGINT | FK → categories.id |
| active | BOOLEAN | DEFAULT true |

### products
| Поле | Тип | Ограничения |
|------|-----|-------------|
| id | BIGSERIAL | PK |
| name | VARCHAR(255) | NOT NULL |
| description | TEXT | |
| price | DECIMAL(10,2) | NOT NULL |
| stock | INTEGER | NOT NULL |
| version | INTEGER | NOT NULL DEFAULT 0 |
| image_url | VARCHAR(255) | |
| category_id | BIGINT | FK → categories.id |
| active | BOOLEAN | DEFAULT true |
| created_at | TIMESTAMP | NOT NULL |

### carts
| Поле | Тип | Ограничения |
|------|-----|-------------|
| id | BIGSERIAL | PK |
| user_id | BIGINT | FK → users.id, UNIQUE |
| session_id | VARCHAR(255) | |
| created_at | TIMESTAMP | NOT NULL |

### cart_items
| Поле | Тип | Ограничения |
|------|-----|-------------|
| id | BIGSERIAL | PK |
| cart_id | BIGINT | FK → carts.id, NOT NULL |
| product_id | BIGINT | FK → products.id, NOT NULL |
| quantity | INTEGER | NOT NULL |
| UNIQUE(cart_id, product_id) | | |

### orders
| Поле | Тип | Ограничения |
|------|-----|-------------|
| id | BIGSERIAL | PK |
| user_id | BIGINT | FK → users.id, NOT NULL |
| order_date | TIMESTAMP | NOT NULL |
| delivery_address | VARCHAR(255) | |
| payment_method | VARCHAR(20) | |
| status | VARCHAR(20) | NOT NULL |
| total_sum | DECIMAL(10,2) | NOT NULL |

### order_items
| Поле | Тип | Ограничения |
|------|-----|-------------|
| id | BIGSERIAL | PK |
| order_id | BIGINT | FK → orders.id, NOT NULL |
| product_id | BIGINT | FK → products.id, NOT NULL |
| quantity | INTEGER | NOT NULL |
| price_at_purchase | DECIMAL(10,2) | NOT NULL |

### audit_logs
| Поле | Тип | Ограничения |
|------|-----|-------------|
| id | BIGSERIAL | PK |
| admin_id | BIGINT | NOT NULL |
| action | VARCHAR(50) | NOT NULL |
| target_id | BIGINT | |
| timestamp | TIMESTAMP | NOT NULL |

## Индексы

| Таблица | Столбец(цы) | Тип |
|---------|-------------|-----|
| users | email | UNIQUE |
| products | name | BTREE (для LOWER поиска) |
| products | category_id | BTREE |
| orders | user_id | BTREE |
| orders | status | BTREE |

## Версионность

- `products.version` — `@Version` для optimistic locking
- Используется при конкурентном списании остатков в заказе

## Liquibase

Миграции в `src/main/resources/db/migration/`:
- Челночные changesets (числовой префикс: `001_...`, `002_...`)
- Dev-профиль: Liquibase отключён, Hibernate `ddl-auto: create-drop`
