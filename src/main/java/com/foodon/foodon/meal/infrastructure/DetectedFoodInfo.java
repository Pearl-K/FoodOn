package com.foodon.foodon.meal.infrastructure;

import com.foodon.foodon.meal.dto.PositionInfo;

import java.math.BigDecimal;
import java.util.List;

public record DetectedFoodInfo(
        String name,
        int count,
        List<PositionInfo> positions
){
}
