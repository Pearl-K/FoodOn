package com.foodon.foodon.meal.dto;

import com.foodon.foodon.meal.domain.Position;

public record PositionInfo(
        double x,
        double y,
        double width,
        double height,
        double confidence
) {
    public static PositionInfo of(Position position){
        return new PositionInfo(
                position.getX(),
                position.getY(),
                position.getWidth(),
                position.getHeight(),
                position.getConfidence()
        );
    }
}
