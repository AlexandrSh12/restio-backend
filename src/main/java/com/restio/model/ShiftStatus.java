package com.restio.model;

public enum ShiftStatus {
    ACTIVE("active"),     // активная смена
    CLOSED("closed");     // закрытая смена

    private final String status;

    ShiftStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}