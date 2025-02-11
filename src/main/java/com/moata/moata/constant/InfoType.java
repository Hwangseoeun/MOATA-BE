package com.moata.moata.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum InfoType {
    NOTICE, FAQ;

    @JsonCreator
    public static InfoType from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("InfoType cannot be null or empty");
        }

        try {
            return InfoType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid InfoType: " + value + ". Allowed values: NOTICE, FAQ");
        }
    }

    @JsonValue
    public String toLowerCase() {
        return this.name().toLowerCase();
    }
}
