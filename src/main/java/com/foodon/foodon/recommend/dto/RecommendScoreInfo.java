package com.foodon.foodon.recommend.dto;

import java.util.List;

public record RecommendScoreInfo(
        RecommendedFood food,
        int score,
        List<String> reasons
) {
}
