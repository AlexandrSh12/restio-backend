package com.restio.model;

public enum OrderStatus {
    DRAFT("draft"),           // черновик
    SUBMITTED("submitted"),   // отправлен на кухню
    PREPARING("preparing"),   // готовится
    READY("ready"),          // готов
    DELIVERED("delivered");   // доставлен

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}