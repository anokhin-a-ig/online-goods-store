package ru.anokhin.dev.onlinegoodsstore.config.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class User {

    @Id
    private UUID id;

    private String name;

    private String lastName;

    private String email;

    private String Username;

    private String password;

    private Role role;

    private LocalDateTime create_at;

    private Boolean isBlocked;

    private LocalDateTime blocked_to;
}
