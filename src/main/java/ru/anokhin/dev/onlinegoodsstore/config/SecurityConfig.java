package ru.anokhin.dev.onlinegoodsstore.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.RequestMatcherProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("prod")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")  // ваша кастомная страница логина
                        .permitAll()
                        .defaultSuccessUrl("/")
                );

        return http.build();
    }
}