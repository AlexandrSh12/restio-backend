package com.restio.model;

public enum OrderItemStatus {
    PENDING("pending"),       // ожидает приготовления
    PREPARING("preparing"),   // готовится
    READY("ready");          // готово

    private final String status;

    OrderItemStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}