package ru.anokhin.dev.onlinegoodsstore.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private Double price;
    @Column
    private double stock_quantity; // остаток на складе
//    @Column
//    private List<Byte[]> bytes; // ссылка на картинку товара
    @Column
    private Category category_id; //
    @Column
    private LocalDateTime created_at;
}
