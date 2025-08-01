package com.foodon.foodon.food.infrastructure;

import com.foodon.foodon.food.domain.FoodNutrientClaim;

import java.util.List;

public interface FoodBatchRepository {
    int[] saveAllFoodNutrientClaims(List<FoodNutrientClaim> foodNutrientClaims);
}
