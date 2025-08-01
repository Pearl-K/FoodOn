package com.foodon.foodon.food.domain.rule;

import com.foodon.foodon.food.dto.NutrientServingInfo;
import com.foodon.foodon.food.domain.NutrientClaimType;
import com.foodon.foodon.food.domain.NutrientCode;
import com.foodon.foodon.food.domain.NutrientLevel;

import java.util.Map;

public interface NutrientClaimRule {
    NutrientClaimType getNutrientClaimType();
    NutrientCode getNutrientCode();
    NutrientLevel getLevel();
    boolean matches(Map<NutrientCode, NutrientServingInfo> nutrientMap);
}

