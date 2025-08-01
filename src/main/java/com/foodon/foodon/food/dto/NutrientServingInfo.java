package com.foodon.foodon.food.dto;

import com.foodon.foodon.food.domain.NutrientCode;

import java.math.BigDecimal;

public record NutrientServingInfo(
        NutrientCode nutrientCode,
        BigDecimal per100g, // 식품 100g 당 수치
        BigDecimal perServing // 1회 제공량 당 수치
) {
}

