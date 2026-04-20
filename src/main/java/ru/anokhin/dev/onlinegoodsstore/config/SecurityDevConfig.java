package ru.anokhin.dev.onlinegoodsstore.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.RequestMatcherProvider;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.AntPathMatcher;

@Configuration
@Profile("dev")
public class SecurityDevConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Разрешаем доступ к H2 консоли (все URL, включая вложенные запросы)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )

                .csrf(httpSecurityCsrfConfigurer ->
                        httpSecurityCsrfConfigurer.ignoringRequestMatchers("/h2-console", "/h2-console/*"))

                // 3. РАЗРЕШАЕМ использование iframe (H2 Console работает во фрейме)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
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