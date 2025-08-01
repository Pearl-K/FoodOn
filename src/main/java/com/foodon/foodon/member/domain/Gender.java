package com.foodon.foodon.member.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    MALE, FEMALE;

    @JsonCreator
    public static Gender from(String value) {
        return Gender.valueOf(value.toUpperCase());
    }
}