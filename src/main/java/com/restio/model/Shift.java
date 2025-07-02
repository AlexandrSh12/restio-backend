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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ShiftStatus status = ShiftStatus.ACTIVE;

    @Column(name = "current_order_number", nullable = false)
    private Integer currentOrderNumber = 0;

    @PrePersist
    private void prePersist() {
        if (this.startDate == null) {
            this.startDate = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = ShiftStatus.ACTIVE;
        }
        if (this.currentOrderNumber == null) {
            this.currentOrderNumber = 0;
        }
    }

    @PreUpdate
    private void preUpdate() {
        // Автоматически устанавливаем время окончания при закрытии смены
        if (this.status == ShiftStatus.CLOSED && this.endDate == null) {
            this.endDate = LocalDateTime.now();
        }
    }
}