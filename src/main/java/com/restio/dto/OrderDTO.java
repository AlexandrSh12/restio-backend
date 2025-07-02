package com.restio.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderDTO {
    private String id;
    private Integer orderNumber; // Порядковый номер заказа
    private Long shiftId; // ID смены
    private List<OrderItemDTO> items;
    private String status;
    private String comment;
}