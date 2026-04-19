package ru.anokhin.dev.onlinegoodsstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Разрешаем доступ к H2 консоли (все URL, включая вложенные запросы)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )

                // 2. ОТКЛЮЧАЕМ CSRF для H2 консоли (это критично!)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
                ) // TODO - обновить deprecate метод

                // 3. РАЗРЕШАЕМ использование iframe (H2 Console работает во фрейме)
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )

                // 4. (Опционально) Если используете форму логина
                .formLogin(form -> form
                        .loginPage("/login")  // ваша кастомная страница логина
                        .permitAll()
                        .defaultSuccessUrl("/")
                );

        return http.build();
    }
}