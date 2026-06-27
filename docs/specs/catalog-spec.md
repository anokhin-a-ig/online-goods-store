# Каталог товаров

## Сущности

- `Category` — `entity/Category.java`
- `Product` — `entity/Product.java`

## Category

| Поле | Тип | Ограничения |
|------|-----|-------------|
| id | Long | PK, автоинкремент |
| name | String | unique, not null |
| description | String | |
| parent | Category (self-ref) | дерево, макс. глубина 2 уровня |
| active | boolean | default true; каскадная деактивация дочерних |

Связи: `@OneToMany → Product`, `@ManyToOne → parent`, `@OneToMany → children`

## Product

| Поле | Тип | Ограничения |
|------|-----|-------------|
| id | Long | PK, автоинкремент |
| name | String | not null, индекс для поиска |
| description | String | полнотекстовый поиск (pg_trgm или LOWER) |
| price | BigDecimal | precision 10, scale 2, RoundingMode.HALF_EVEN |
| stock | int | количество на складе |
| version | int | @Version — optimistic locking |
| imageUrl | String | ссылка на изображение, JPEG/PNG макс 5MB |
| active | boolean | soft-delete, default true |
| createdAt | LocalDateTime | |
| category | ManyToOne → Category | |

Связи: `@ManyToOne → Category`

## Функциональность

### Список товаров (пагинированный)
- Сортировка: по цене, названию, дате добавления
- Фильтры: по категории, диапазону цены, наличию (stock > 0)
- Пагинация: page, size, sort

### Карточка товара
- Название, цена, описание, изображение, остаток
- Placeholder при недоступности изображения

### Поиск
- Регистронезависимый по названию и описанию
- LOWER() + индекс или pg_trgm

### CRUD (админ)
- Создание, редактирование, удаление (soft-delete через active)
- Загрузка изображения

## DAO

- `CategoryDao` — id, name, description, parentId, parentName, active
- `ProductDao` — id, name, description, price, stock, imageUrl, active, createdAt, categoryId, categoryName
