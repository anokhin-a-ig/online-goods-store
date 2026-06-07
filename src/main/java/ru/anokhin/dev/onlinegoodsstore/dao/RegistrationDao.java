package ru.anokhin.dev.onlinegoodsstore.dao;

import lombok.Data;
import org.springframework.stereotype.Component;

public record RegistrationDao(
        String username,
        String password
) {
}
