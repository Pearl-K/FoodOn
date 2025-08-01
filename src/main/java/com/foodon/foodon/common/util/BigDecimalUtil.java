package com.foodon.foodon.common.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class BigDecimalUtil {

    private static final int DEFAULT_SCALE = 2;

    public static BigDecimal safe(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    public static BigDecimal multiply(BigDecimal base, BigDecimal multiplier) {
        return safe(base).multiply(multiplier != null ? multiplier : BigDecimal.ONE);
    }

    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        if (b == null || b.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("0으로 나눌 수 없습니다.");
        }
        return safe(a).divide(b, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal divide(BigDecimal a, int scale, BigDecimal b) {
        if (b == null || b.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("0으로 나눌 수 없습니다.");
        }
        return safe(a).divide(b, scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return safe(a).add(safe(b));
    }

    // 반올림하여 정수형 변환
    public static int toRoundedInt(BigDecimal value) {
        return value.setScale(0, RoundingMode.HALF_UP).intValue();
    }

    public static BigDecimal round(BigDecimal value, int scale) {
        return value != null
                ? value.setScale(scale, RoundingMode.HALF_UP)
                : BigDecimal.ZERO.setScale(scale, RoundingMode.HALF_UP);
    }
}
