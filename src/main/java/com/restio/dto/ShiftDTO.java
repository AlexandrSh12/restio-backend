package com.restio.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShiftDTO {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;                  // ACTIVE, CLOSED
    private Integer currentOrderNumber;
}