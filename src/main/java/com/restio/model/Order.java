package com.restio.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    private String id; // UUID с фронта или генерируется автоматически

    @Column(name = "order_number", nullable = false)
    private Integer orderNumber; // Порядковый номер в смене

    @ManyToOne
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shift; // Связь со сменой

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    private String status; // draft, submitted, preparing, ready, delivered
    private String comment;

    @PrePersist
    private void generateIdIfNull() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = UUID.randomUUID().toString();
        }
    }
}