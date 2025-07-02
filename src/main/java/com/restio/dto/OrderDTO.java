package com.restio.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private String id;
    private Integer orderNumber;
    private Long shiftId;
    private Long waiterId;              // ID официанта
    private String waiterName;          // Имя официанта для отображения
    private List<OrderItemDTO> items;
    private String status;
    private String comment;

    // Временные метки
    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;
    private LocalDateTime readyAt;
    private LocalDateTime deliveredAt;
}