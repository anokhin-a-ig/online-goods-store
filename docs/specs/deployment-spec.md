# Развёртывание

## Docker

### docker-compose.yaml
```yaml
services:
  app:
    image: online-goods-store:latest
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_HOST=postgres
      - DB_PORT=5432
      - DB_NAME=STORE
      - DB_USER=postgres
      - DB_PASSWORD=postgres
    depends_on:
      postgres:
        condition: service_healthy
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/online-goods-store/actuator/health"]
      interval: 30s
      timeout: 5s
      retries: 3

  postgres:
    image: postgres:16
    environment:
      POSTGRES_DB: STORE
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  pgdata:
```

### Dockerfile
```dockerfile
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/online-goods-store-*.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD curl -f http://localhost:8080/online-goods-store/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Профили

### dev
- Порт: 8081
- PostgreSQL: localhost:5432/STORE
- Hibernate ddl-auto: create-drop
- Liquibase: disabled
- Spring Security default: user/user

### prod
- Порт: 8080
- PostgreSQL: настраивается через env
- Hibernate ddl-auto: validate
- Liquibase: enabled (update)
- Актуатор: health, info, metrics

### test
- H2 in-memory или test PostgreSQL
- Liquibase: enabled

## CI/CD (Jenkins)

`pipeline.groovy`:
```
cleanWs
mvn --version
checkout */master
mvn clean package -DskipTests=true
```

## Переменные окружения

| Переменная | Описание | Значение по умолчанию |
|-----------|----------|----------------------|
| SPRING_PROFILES_ACTIVE | активный профиль | dev |
| DB_HOST | хост PostgreSQL | localhost |
| DB_PORT | порт PostgreSQL | 5432 |
| DB_NAME | имя БД | STORE |
| DB_USER | пользователь БД | postgres |
| DB_PASSWORD | пароль БД | postgres |

## Health Check

`/online-goods-store/actuator/health` — liveness probe для Docker
