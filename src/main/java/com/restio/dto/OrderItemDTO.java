package com.restio.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderItemDTO {
    private Long id;
    private Long dishId;
    private String dishName;
    private Integer count;              // Изменен с int на Integer
    private BigDecimal price;           // Изменен с int на BigDecimal
    private String status;              // Статус позиции (pending, preparing, ready)
    private String comment;             // Комментарий к позиции

    // Временные метки
    private LocalDateTime startCookingAt;
    private LocalDateTime readyAt;
}