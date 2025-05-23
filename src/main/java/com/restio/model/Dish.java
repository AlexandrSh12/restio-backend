package com.restio.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category categoryEntity;
    // Строковое представление категории для обратной совместимости
    private String category;
    private int price;
    private int cookTime;
    private String description;
    private boolean available = true;
}