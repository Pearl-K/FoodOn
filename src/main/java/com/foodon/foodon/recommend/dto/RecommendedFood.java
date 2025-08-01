package com.foodon.foodon.recommend.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.foodon.foodon.common.util.BigDecimalUtil;
import com.foodon.foodon.food.domain.NutrientCode;
import com.foodon.foodon.food.domain.Unit;
import com.foodon.foodon.food.dto.NutrientInfo;

public record RecommendedFood(
	Long foodId,
	String foodName,
	BigDecimal servingSize,
	Unit unit,
	BigDecimal kcal,
	Map<NutrientCode, NutrientInfo> nutrients
) {
public static RecommendedFood from(
	Long foodId,
	String foodName,
	BigDecimal servingSize,
	Unit unit,
	List<NutrientInfo> nutrientList
) {
	Map<NutrientCode,NutrientInfo> nutrients = new HashMap<>();
	for(NutrientInfo nutrient : nutrientList) {
		nutrients.put(nutrient.code(), nutrient);
	}

	NutrientInfo kcalInfo = nutrients.get(NutrientCode.KCAL);
	BigDecimal value = kcalInfo == null?BigDecimal.ZERO:kcalInfo.value();
	BigDecimal kcal = BigDecimalUtil.divide(
		BigDecimalUtil.multiply(servingSize, value),
		BigDecimal.valueOf(100)
	);

	return new RecommendedFood(
		foodId,
		foodName,
		servingSize,
		unit,
		kcal,
		nutrients
	);
}
}
