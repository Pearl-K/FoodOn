package com.foodon.foodon.member.dto;

import com.foodon.foodon.member.domain.NutrientPlan;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public record NutrientPlanResponse(
        Long id,
        String name,
        String description
) {
    public static NutrientPlanResponse of(NutrientPlan nutrientPlan) {
        return new NutrientPlanResponse(
                nutrientPlan.getId(),
                nutrientPlan.getName(),
                nutrientPlan.getDescription()
        );
    }
}
