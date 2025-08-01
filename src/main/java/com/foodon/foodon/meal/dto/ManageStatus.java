package com.foodon.foodon.meal.dto;

import java.math.BigDecimal;

public enum ManageStatus {
    DANGER, // 위험
    DEFICIENT, // 부족
    ADEQUATE, // 적정
    CAUTION // 주의
    ;

    public static ManageStatus evaluate(Double intake, Double min, Double max) {
        if (min != null && intake < min) return DEFICIENT;
        if (max != null && intake > max * 1.5) return DANGER;
        if (max != null && intake > max) return CAUTION;
        return ADEQUATE;
    }
}
