package com.foodon.foodon.member.dto;

import com.foodon.foodon.member.domain.ActivityLevel;

public record ActivityLevelResponse(
        Long id,
        String description,
        float value
) {
    public static ActivityLevelResponse of(ActivityLevel activityLevel) {
        return new ActivityLevelResponse(
                activityLevel.getId(),
                activityLevel.getDescription(),
                activityLevel.getValue()
        );
    }
}
