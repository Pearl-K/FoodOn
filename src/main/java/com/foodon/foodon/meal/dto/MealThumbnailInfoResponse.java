package com.foodon.foodon.meal.dto;

public record MealThumbnailInfoResponse(
        Long mealId,
        Long mealItemId,
        String foodName,
        String mealImage,
        PositionInfo positionInfo
) {
    public static MealThumbnailInfoResponse of(
            MealThumbnailInfo mealThumbnailInfo
    ){
        return new MealThumbnailInfoResponse(
                mealThumbnailInfo.mealId(),
                mealThumbnailInfo.mealItemId(),
                mealThumbnailInfo.foodName(),
                mealThumbnailInfo.mealImage(),
                mealThumbnailInfo.positionInfo()
        );
    }
}
