package com.restio.dto;

import lombok.Data;

@Data
public class DishDTO {
    private Long id;
    private String name;
    private String category;
    private int price;
    private int cookTime;
    private String imageUrl;
}
