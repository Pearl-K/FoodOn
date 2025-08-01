package com.foodon.foodon.food.domain.rule;

import com.foodon.foodon.food.dto.NutrientServingInfo;
import com.foodon.foodon.food.domain.NutrientClaimType;
import com.foodon.foodon.food.domain.NutrientCode;
import com.foodon.foodon.food.domain.NutrientLevel;

import java.math.BigDecimal;
import java.util.Map;

public class LowSugarRule implements NutrientClaimRule {

    @Override
    public NutrientClaimType getNutrientClaimType() {
        return NutrientClaimType.LOW_SUGAR;
    }

    @Override
    public NutrientCode getNutrientCode() {
        return NutrientCode.SUGAR;
    }

    @Override
    public NutrientLevel getLevel() {
        return NutrientLevel.LOW;
    }

    @Override
    public boolean matches(Map<NutrientCode, NutrientServingInfo> nutrientMap) {
        if(!nutrientMap.containsKey(NutrientCode.SUGAR)){
            return false;
        }

        NutrientServingInfo sugarInfo = nutrientMap.get(NutrientCode.SUGAR);
        return sugarInfo.per100g().compareTo(BigDecimal.valueOf(5)) < 0;
    }
}

