package ru.urfu.lr3.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Codes {
    SUCCESS("success"),
    FAILURE("failure");
    private final String name;
    Codes(String name) {
        this.name = name;
    }
    @JsonValue
    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return name;
    }
}
