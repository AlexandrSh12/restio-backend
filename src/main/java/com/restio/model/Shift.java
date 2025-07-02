package com.restio.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "shifts")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE, CLOSED

    @Column(name = "current_order_number", nullable = false)
    private Integer currentOrderNumber = 0;

    public Shift() {
        this.startDate = LocalDateTime.now();
        this.status = ShiftStatus.ACTIVE;
        this.currentOrderNumber = 0;
    }
}