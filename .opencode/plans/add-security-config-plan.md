# План подключения и настройки Spring Security

## Общее описание

Подключение Spring Security к проекту `online-goods-store` (Spring Boot 3.5.13, Java 17).
Аутентификация через Form Login, пароли BCrypt, сессии (не JWT). Роли: `ROLE_USER`, `ROLE_ADMIN`.

## Порядок выполнения (оптимальная последовательность)

### Шаг 1. Репозиторий UserRepository

**Файл:** `src/main/java/ru/anokhin/dev/onlinegoodsstore/repository/UserRepository.java`

```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
```

Базовый CRUD уже есть от Spring Data JPA, добавляем только `findByEmail`.

---

### Шаг 2. PasswordEncoder + UserDetailsService

**Файл:** `src/main/java/ru/anokhin/dev/onlinegoodsstore/config/security/SecurityBeansConfig.java`

```java
@Configuration
public class SecurityBeansConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

**Файл:** `src/main/java/ru/anokhin/dev/onlinegoodsstore/service/security/CustomUserDetailsService.java`

Реализует `UserDetailsService.loadUserByUsername(String email)`:
- Ищет `User` по email через `UserRepository`
- Проверяет `blocked == true` → `DisabledException("Учётная запись заблокирована администратором")`
- Проверяет `lockTime != null && lockTime.isAfter(LocalDateTime.now())` → `LockedException("Учётная запись временно заблокирована до %s")`
- Возвращает `org.springframework.security.core.userdetails.User` с GrantedAuthority из `role`

---

### Шаг 3. Сервис аутентификации (логика блокировок)

**Файл:** `src/main/java/ru/anokhin/dev/onlinegoodsstore/service/security/AuthenticationService.java`

```java
@Service
@Transactional
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Регистрация нового пользователя
    public User register(String email, String password, String name, String deliveryAddress) {
        // Валидация пароля: min 8 символов, минимум 1 цифра + 1 лат. буква
        // if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d).{8,}$"))
        //     throw new IllegalArgumentException("...")
        // Создание User с role = ROLE_USER, password = BCrypt
        // Сохранение в БД
    }

    // Успешная аутентификация — сброс счётчиков
    public void onAuthenticationSuccess(String email) {
        // failedAttempts = 0, lockTime = null
    }

    // Неудачная аутентификация — инкремент счётчика
    public void onAuthenticationFailure(String email) {
        // failedAttempts++
        // если failedAttempts >= 5 → lockTime = now + 15 min
    }
}
```

---

### Шаг 4. SecurityConfig (основной)

**Файл:** `src/main/java/ru/anokhin/dev/onlinegoodsstore/config/security/SecurityConfig.java`

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity   // для @PreAuthorize на контроллерах
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/actuator/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/auth/login?error")
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout")
                .addLogoutHandler(customLogoutHandler())
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
            )
            .sessionManagement(session -> session
                .sessionFixation().migrateSession()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            )
            .csrf(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CustomLogoutHandler customLogoutHandler() {
        return new CustomLogoutHandler(auditLogRepository);
    }
}
```

---

### Шаг 5. LogoutHandler (аудит выхода ADMIN)

**Файл:** `src/main/java/ru/anokhin/dev/onlinegoodsstore/config/security/CustomLogoutHandler.java`

```java
@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
        if (authentication != null) {
            boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin) {
                String email = authentication.getName();
                User user = userRepository.findByEmail(email).orElse(null);
                if (user != null) {
                    AuditLog log = new AuditLog();
                    log.setAdminId(user.getId());
                    log.setAction("LOGOUT");
                    auditLogRepository.save(log);
                }
            }
        }
    }
}
```

**Файл:** `src/main/java/ru/anokhin/dev/onlinegoodsstore/repository/AuditLogRepository.java`

```java
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
```

---

### Шаг 6. RateLimitFilter (OncePerRequestFilter, без Bucket4j)

**Файл:** `src/main/java/ru/anokhin/dev/onlinegoodsstore/config/security/RateLimitFilter.java`

```java
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, AtomicInteger> attempts = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> resetTimes = new ConcurrentHashMap<>();

    private static final int MAX_ATTEMPTS = 10;
    private static final long RESET_INTERVAL_SECONDS = 60;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        if (request.getRequestURI().contains("/auth/login")
                && "POST".equalsIgnoreCase(request.getMethod())) {

            String ip = request.getRemoteAddr();
            LocalDateTime now = LocalDateTime.now();

            // Сброс счётчика, если прошло больше интервала
            LocalDateTime resetTime = resetTimes.get(ip);
            if (resetTime == null || now.isAfter(resetTime)) {
                attempts.put(ip, new AtomicInteger(1));
                resetTimes.put(ip, now.plusSeconds(RESET_INTERVAL_SECONDS));
            } else {
                int count = attempts.get(ip).incrementAndGet();
                if (count > MAX_ATTEMPTS) {
                    response.setStatus(429);
                    response.getWriter().write("Слишком много попыток входа. Попробуйте позже.");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
```

Регистрация в SecurityConfig:

```java
.addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
```

---

### Шаг 7. CORS (WebMvcConfigurer)

**Файл:** `src/main/java/ru/anokhin/dev/onlinegoodsstore/config/WebConfig.java`

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:8080")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
```

---

### Шаг 8. Контроллер аутентификации (AuthController)

**Файл:** `src/main/java/ru/anokhin/dev/onlinegoodsstore/controllers/mvc/AuthController.java`

```java
@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";  // Thymeleaf шаблон (будет создан позже)
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegistrationRequest request,
                           BindingResult result) {
        if (result.hasErrors()) return "auth/register";
        authenticationService.register(...);
        return "redirect:/auth/login?registered";
    }
}
```

---

### Шаг 9. DTO для регистрации

**Файл:** `src/main/java/ru/anokhin/dev/onlinegoodsstore/dao/RegistrationRequest.java`

```java
public record RegistrationRequest(
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8) String password,
    @NotBlank String name,
    String deliveryAddress
) {}
```

---

### Шаг 10. Обновление application-dev.yml

Удалить блок `spring.security.user.*` — теперь аутентификация через БД.

```yaml
# Было:
# spring:
#   security:
#     user:
#       name: user
#       password: user
# Удалить полностью
```

---

### Шаг 11. (Опционально) Создать Liquibase changeset для добавления тестового ADMIN

**Файл:** `src/main/resources/db/changelog/changelog.xml` (если существует)

```sql
-- Вставка тестового пользователя для dev-среды:
INSERT INTO users (email, password, name, role, blocked, failed_attempts, created_at)
VALUES ('admin@store.dev', '$2a$10$...', 'Admin', 'ROLE_ADMIN', false, 0, NOW());
```

---

## Сводная таблица новых файлов

| # | Файл | Шаг |
|---|------|-----|
| 1 | `repository/UserRepository.java` | 1 |
| 2 | `repository/AuditLogRepository.java` | 5 |
| 3 | `config/security/SecurityBeansConfig.java` | 2 |
| 4 | `config/security/SecurityConfig.java` | 4 |
| 5 | `config/security/CustomLogoutHandler.java` | 5 |
| 6 | `config/security/RateLimitFilter.java` | 6 |
| 7 | `config/WebConfig.java` | 7 |
| 8 | `service/security/CustomUserDetailsService.java` | 2 |
| 9 | `service/security/AuthenticationService.java` | 3 |
| 10 | `controllers/mvc/AuthController.java` | 8 |
| 11 | `dao/RegistrationRequest.java` | 9 |

## Изменяемые файлы

| # | Файл | Что меняется |
|---|------|-------------|
| 1 | `application-dev.yml` | Удалить `spring.security.user.*` |

## Связи между компонентами

```
SecurityConfig
  ├── SecurityBeansConfig.passwordEncoder()        BCrypt
  ├── CustomUserDetailsService                     загрузка User из БД
  │     └── UserRepository.findByEmail()
  ├── AuthenticationService                        логика блокировок
  │     ├── UserRepository
  │     └── PasswordEncoder
  ├── CustomLogoutHandler                          аудит выхода ADMIN
  │     └── AuditLogRepository
  ├── RateLimitFilter                              ограничение /login (10/мин)
  └── AuthController                               /auth/*
        └── AuthenticationService.register()

WebConfig                                          CORS
```
