package com.foodon.foodon.food.infrastructure;

import com.foodon.foodon.food.domain.FoodNutrient;

import java.util.List;

public interface FoodNutrientBatchRepository {
    int[] saveAllFoodNutrients(List<FoodNutrient> foodNutrients);
}
