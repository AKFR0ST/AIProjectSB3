package com.sb3.constant;

public enum TeacherStatus {
    ACTIVE("Активен"),
    INACTIVE("Отключен");

    private final String displayName;

    TeacherStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
