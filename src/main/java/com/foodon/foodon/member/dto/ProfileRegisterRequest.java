package com.foodon.foodon.member.dto;

import com.foodon.foodon.member.domain.Gender;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProfileRegisterRequest(
        @NotNull
        Gender gender,

        @NotNull
        Long managementType,

        @NotNull
        Long activityType,

        @NotNull
        @Min(value = 1, message = "키는 최소 1 이상이어야 합니다")
        Integer height,

        @NotNull
        @Min(value = 1, message = "체중은 최소 1 이상이어야 합니다")
        Integer weight,

        @NotNull
        @Min(value = 1, message = "체중은 최소 1 이상이어야 합니다")
        Integer goalWeight
) {
}
