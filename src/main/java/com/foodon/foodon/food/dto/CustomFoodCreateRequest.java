package com.foodon.foodon.food.dto;

import com.foodon.foodon.food.domain.Unit;
import com.foodon.foodon.meal.dto.NutrientProfile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CustomFoodCreateRequest(
        @NotBlank(message = "음식 이름은 공백이 입력될 수 없습니다.")
        String foodName,

        @NotNull
        Unit unit,

        @PositiveOrZero(message = "중량은 0 이상이어야 합니다.")
        BigDecimal servingSize, // 중량

        @Valid
        @NotNull
        NutrientProfile nutrients
) {
}
