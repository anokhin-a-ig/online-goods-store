package ru.anokhin.dev.onlinegoodsstore.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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
