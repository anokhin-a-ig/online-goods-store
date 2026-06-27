# AGENTS.md — online-goods-store

## Project

Spring Boot 3.5.13, Java 17, Maven. Online goods store with Thymeleaf frontend, PostgreSQL, Spring Security, Liquibase.

- **Group:** `ru.anokhin.dev`, **artifact:** `online-goods-store`, **version:** `0.0.1-SNAPSHOT`
- **Server context path:** `/online-goods-store`
- **Maven wrapper:** `./mvnw` (`mvnw.cmd` on Windows)

## Source structure

All Java under `ru.anokhin.dev.onlinegoodsstore`.

| Layer | Package |
|---|---|
| Entrypoint | `OnlineGoodsStoreApplication.java` |
| REST controllers | `controllers.rest.*` |
| MVC controllers | `controllers.mvc.*` |
| DTO / DAO | `dao.*` |
| Services | `service.*` |
| Repositories | `repository.*` |
| Entities | `entity.*` |
| Config | `config.*` |
| Exceptions | `exception.*` |

## Commands

| Action | Command |
|---|---|
| Build (skip tests) | `mvn clean package -DskipTests=true` |
| All tests | `mvn clean test` |
| Single test | `mvn test -Dtest=TestClassName` |
| Dev run | `docker compose up -d` then `mvn spring-boot:run -Dspring-boot.run.profiles=dev` |
| Run profile | `mvn spring-boot:run -Dspring-boot.run.profiles=<profile>` |
| Lint / compile | `mvn compile` |

Defined in `opencode.json` — use `opencode build/test/dev/run/lint` when available.

## Profiles

Dev profile (`application-dev.yml`):
- Port **8081**, PostgreSQL `localhost:5432/STORE`, user/pass: `postgres/postgres`
- Hibernate `ddl-auto: create-drop`, Liquibase disabled
- Spring Security default: `user / user`
- Base `application.yml` (no profile selected): port **8080**, no DB config

`server.address: localhost` must be changed to `0.0.0.0` for Docker — see `.opencode/plan`.

## State of the repo

- Source files are **deleted from the working tree** on the current branch (`feature/test-ai-agent`). Only `OnlineGoodsStoreApplication.java` remains.
- Compiled `.class` files exist in `target/`. Restore sources with `git checkout <branch> -- src/`.
- `Dockerfile` and `docker-compose.yaml` also deleted — recreate from `.opencode/plan` if needed.
- Missing `application-test.yml` and `application-prod.yml` on disk (prod config only in `target/classes/`).

## Testing

- JUnit 5 + Mockito: `@SpringBootTest`, `@WebMvcTest`, `@DataJpaTest`, `MockMvc`
- `@DisplayName` in Russian
- Context-loads test currently **commented out**
- Use `@ExtendWith(MockitoExtension.class)` for pure unit tests

## Key conventions

- **Prices:** `BigDecimal` with `RoundingMode.HALF_EVEN`
- **Soft delete:** `active` boolean on `Product`, `blocked` on `User`
- **Optimistic locking:** `@Version int version` on `Product`
- **Error responses:** unified `@RestControllerAdvice` (`GlobalExceptionHandler`)
- **API response format:** `{ "data": ..., "timestamp": "..." }` for success, `{ "status": ..., "error": ..., "message": ..., "path": ..., "timestamp": "..." }` for errors
- **Lombok:** used on entities (`@Data`, `@Builder`, etc.) — configure annotation processing in IDE
- **Russian** documentation: `README.md`, `technical_specification.md`, most comments and test `@DisplayName`

## References

- `opencode.json` registers `technical_specification.md` as reference `spec` and `README.md` as reference `readme`
- `.opencode/skills/code-review/SKILL.md` has detailed review checklist (layers, security, JPA, tests)
- `technical_specification.md` contains full entity schemas, API endpoints, DB table definitions, and security requirements

## CI

Jenkins pipeline in `pipeline.groovy`: `cleanWs` → `mvn --version` → checkout `*/master` → `mvn clean package -DskipTests=true`.
