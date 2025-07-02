package com.restio.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DishDTO {
    private Long id;
    private String name;
    private Long categoryId;        // ID категории
    private String categoryName;    // Название категории для отображения
    private BigDecimal price;       // Изменен с int на BigDecimal
    private Integer cookTime;       // Изменен с int на Integer
    private String description;
    private boolean available = true;
    private String imageUrl;
}