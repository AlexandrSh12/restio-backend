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
    private String category;
    private int price;
    private int cookTime;


}
