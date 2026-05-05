package ru.anokhin.dev.onlinegoodsstore.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity (name = "Person")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName; // имя
    @Column
    private String middleName; // отчество
    @Column
    private String lastName; // фамилия
    @Column
    private LocalDate birthDate;
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
