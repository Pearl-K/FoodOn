package com.foodon.foodon.food.domain.rule;

import com.foodon.foodon.food.dto.NutrientServingInfo;
import com.foodon.foodon.food.domain.NutrientClaimType;
import com.foodon.foodon.food.domain.NutrientCode;
import com.foodon.foodon.food.domain.NutrientLevel;

import java.math.BigDecimal;
import java.util.Map;

public class LowKcalRule implements NutrientClaimRule {
    @Override
    public NutrientClaimType getNutrientClaimType() {
        return NutrientClaimType.LOW_KCAL;
    }

    @Override
    public NutrientCode getNutrientCode() {
        return NutrientCode.KCAL;
    }

    @Override
    public NutrientLevel getLevel() {
        return NutrientLevel.LOW;
    }

    @Override
    public boolean matches(Map<NutrientCode, NutrientServingInfo> nutrientMap) {
        if(!nutrientMap.containsKey(NutrientCode.KCAL)){
            return false;
        }

        NutrientServingInfo kcalInfo = nutrientMap.get(NutrientCode.KCAL);
        return kcalInfo.per100g().compareTo(BigDecimal.valueOf(40)) < 0;
    }
}

