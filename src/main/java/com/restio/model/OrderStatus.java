package com.restio.model;

/**
 * Константы для статусов заказа
 */
public class OrderStatus {
    public static final String DRAFT = "draft";
    public static final String SUBMITTED = "submitted";
    public static final String PREPARING = "preparing";
    public static final String READY = "ready";
    public static final String DELIVERED = "delivered";

    // Приватный конструктор, чтобы избежать создания экземпляров
    private OrderStatus() {}
}