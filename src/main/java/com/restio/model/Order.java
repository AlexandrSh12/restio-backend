package com.restio.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    private String id; // UUID с фронта или генерируется автоматически

    @Column(name = "order_number", nullable = true)
    private Integer orderNumber; // Порядковый номер в смене

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", nullable = true)
    private Shift shift; // Связь со сменой

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waiter_id", nullable = true)
    private User waiter; // Официант, создавший заказ

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.DRAFT;

    private String comment; // комментарий к заказу

    @Column(name = "created_at", nullable = true)
    private LocalDateTime createdAt;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "ready_at")
    private LocalDateTime readyAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @PrePersist
    private void prePersist() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    private void preUpdate() {
        // Автоматически устанавливаем временные метки при изменении статуса
        if (this.status == OrderStatus.SUBMITTED && this.submittedAt == null) {
            this.submittedAt = LocalDateTime.now();
        } else if (this.status == OrderStatus.READY && this.readyAt == null) {
            this.readyAt = LocalDateTime.now();
        } else if (this.status == OrderStatus.DELIVERED && this.deliveredAt == null) {
            this.deliveredAt = LocalDateTime.now();
        }
    }
}