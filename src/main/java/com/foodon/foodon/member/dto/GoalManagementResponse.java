package com.foodon.foodon.member.dto;

import com.foodon.foodon.common.util.NutrientGoal;
import com.foodon.foodon.member.domain.ActivityLevel;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.member.domain.MemberStatus;
import com.foodon.foodon.member.domain.NutrientPlan;

public record GoalManagementResponse(
        String managementTypeName,
        int targetCalories,
        int carbRatio,
        int proteinRatio,
        int fatRatio,
        int height,
        int currentWeight,
        int goalWeight
) {
    public static GoalManagementResponse from(
            Member member,
            MemberStatus status,
            ActivityLevel activityLevel,
            NutrientPlan plan
    ) {
        int carbsPercent = (int) Math.round(plan.getCarbsRatio() * 100);
        int proteinPercent = (int) Math.round(plan.getProteinRatio() * 100);
        int fatPercent = 100 - carbsPercent - proteinPercent;
        NutrientGoal goal = NutrientGoal.from(member, status, activityLevel, plan);

        return new GoalManagementResponse(
                plan.getName(),
                goal.getGoalKcal().intValue(),
                carbsPercent,
                proteinPercent,
                fatPercent,
                status.getHeight(),
                status.getWeight(),
                status.getGoalWeight()
        );
    }
}

