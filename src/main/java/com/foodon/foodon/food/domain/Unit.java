package com.foodon.foodon.food.domain;

import lombok.Getter;

@Getter
public enum Unit {

    // 피자, 빵
    SLICE("조각"),

    // 일반 식사 단위
    SERVING("인분"),
    BOWL("그릇"),
    PLATE("접시"),

    // 치킨
    WHOLE("마리"),

    // 음료, 액체류
    BOTTLE("병"),
    CAN("캔"),
    CUP("컵"),
    ML("ml"),
    LITER("L"),

    PIECE("개"),
    BAG("봉지"),
    SHEET("장"),
    STRING("줄"),
    EGG("알"),

    GRAM("그램"),
    KILOGRAM("킬로그램"),

    NONE("없음");

    private final String displayName;

    Unit(String displayName) {
        this.displayName = displayName;
    }

}
