package com.foodon.foodon.meal.infrastructure;

import java.util.List;

public record MealDetectAiResponse(
        List<DetectedFoodInfo> food
) {
}
