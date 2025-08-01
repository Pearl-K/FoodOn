package com.foodon.foodon.food.domain.rule;

import com.foodon.foodon.food.dto.NutrientServingInfo;
import com.foodon.foodon.food.domain.NutrientClaimType;
import com.foodon.foodon.food.domain.NutrientCode;
import com.foodon.foodon.food.domain.NutrientLevel;

import java.math.BigDecimal;
import java.util.Map;

public class HighProteinRule implements NutrientClaimRule {
    @Override
    public NutrientClaimType getNutrientClaimType() {
        return NutrientClaimType.HIGH_PROTEIN;
    }

    @Override
    public NutrientCode getNutrientCode() {
        return NutrientCode.PROTEIN;
    }

    @Override
    public NutrientLevel getLevel() {
        return NutrientLevel.HIGH;
    }

    /**
     * 식품 100g 당 함량이 1일 권장량의 20% 이상인 경우 고단백
     */
    @Override
    public boolean matches(Map<NutrientCode, NutrientServingInfo> nutrientMap) {
        if(!nutrientMap.containsKey(NutrientCode.PROTEIN)){
            return false;
        }

        NutrientServingInfo proteinInfo = nutrientMap.get(NutrientCode.PROTEIN);
        return proteinInfo.per100g().compareTo(BigDecimal.valueOf(10)) >= 0;
    }
}

