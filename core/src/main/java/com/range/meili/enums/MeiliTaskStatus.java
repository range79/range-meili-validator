package com.range.meili.enums;
public enum MeiliTaskStatus {

    ENQUEUED,
    PROCESSING,
    SUCCEEDED,
    FAILED,
    CANCELED,
    UNKNOWN;

    public static MeiliTaskStatus from(String value) {
        if (value == null || value.isBlank()) {
            return UNKNOWN;
        }

        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return UNKNOWN;
        }
    }
}
