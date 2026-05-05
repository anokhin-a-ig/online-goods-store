package ru.anokhin.dev.onlinegoodsstore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;

// CartItem.java
@Entity(name = "CartItem")
@Data
public class CartItem {
    @Id
    @GeneratedValue
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @Column
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @Column
    private Product product;

    @Min(1)
    @Column
    private int quantity;

    @Column(unique = true)  // (cart_id, product_id) уникальность
    private String uniqueConstraint;  // или через @UniqueConstraint в классе
}