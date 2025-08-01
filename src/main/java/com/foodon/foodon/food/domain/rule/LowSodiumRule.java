package com.foodon.foodon.food.domain.rule;

import com.foodon.foodon.food.dto.NutrientServingInfo;
import com.foodon.foodon.food.domain.NutrientClaimType;
import com.foodon.foodon.food.domain.NutrientCode;
import com.foodon.foodon.food.domain.NutrientLevel;

import java.math.BigDecimal;
import java.util.Map;

public class LowSodiumRule implements NutrientClaimRule {
    @Override
    public NutrientClaimType getNutrientClaimType() {
        return NutrientClaimType.LOW_SODIUM;
    }

    @Override
    public NutrientCode getNutrientCode() {
        return NutrientCode.SODIUM;
    }

    @Override
    public NutrientLevel getLevel() {
        return NutrientLevel.LOW;
    }

    @Override
    public boolean matches(Map<NutrientCode, NutrientServingInfo> nutrientMap) {
        if(!nutrientMap.containsKey(NutrientCode.SODIUM)){
            return false;
        }

        NutrientServingInfo sodiumInfo = nutrientMap.get(NutrientCode.SODIUM);
        return sodiumInfo.per100g().compareTo(BigDecimal.valueOf(120)) < 0;
    }
}

