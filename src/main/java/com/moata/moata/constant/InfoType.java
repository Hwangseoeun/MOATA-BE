package com.moata.moata.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum InfoType {
    NOTICE, FAQ;

    @JsonCreator
    public static InfoType from(String value) {
        return InfoType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toLowerCase() {
        return this.name().toLowerCase();
    }
}
