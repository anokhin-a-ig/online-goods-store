package ru.anokhin.dev.onlinegoodsstore.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity (name = "Person")
@Data
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column
    private String firstName; // имя
    @Column
    private String middleName; // отчество
    @Column
    private String lastName; // фамилия
    @Column
    private int age; // возраст
    @OneToOne
    private User user; // Логин+пароль
    @Column
    private String street; // Улица
    @Column
    private String house; // Дом
    @Column
    private String housePart; // корпус
    @Column
    private String apartment; // квартира
}
