package com.restio.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    private Long dishId;
    private String dishName;
    private int count;
    private int price;
}