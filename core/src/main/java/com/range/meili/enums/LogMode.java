package com.range.meili.enums;

public enum LogMode {
    INFO,
    WARN,
    ERROR,
    DEBUG;

    public static final LogMode DEFAULT = INFO;

    public static LogMode from(String value) {
        if (value == null || value.isBlank()) {
            return DEFAULT;
        }
        try {
            return LogMode.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return DEFAULT;
        }
    }
}
