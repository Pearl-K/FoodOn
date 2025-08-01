package com.foodon.foodon.common.util;

import com.foodon.foodon.food.domain.NutrientCode;
import com.foodon.foodon.member.domain.*;
import lombok.Getter;

import java.math.BigDecimal;

import static com.foodon.foodon.common.util.BigDecimalUtil.*;

@Getter
public class NutrientGoal {

    private static final BigDecimal KCAL_PER_GRAM_CARBS = BigDecimal.valueOf(4);
    private static final BigDecimal KCAL_PER_GRAM_PROTEIN = BigDecimal.valueOf(4);
    private static final BigDecimal KCAL_PER_GRAM_FAT = BigDecimal.valueOf(9);
    private static final int TARGET_DAYS = 90; // 목표 일수는 현재 고정

    private final BigDecimal goalKcal;
    private final BigDecimal goalCarbs;
    private final BigDecimal goalProtein;
    private final BigDecimal goalFat;

    private NutrientGoal(
            BigDecimal goalKcal,
            BigDecimal goalCarbs,
            BigDecimal goalProtein,
            BigDecimal goalFat
    ){
        this.goalKcal = goalKcal;
        this.goalCarbs = goalCarbs;
        this.goalProtein = goalProtein;
        this.goalFat = goalFat;
    }

    public static NutrientGoal from(
            BigDecimal goalKcal,
            NutrientPlan nutrientPlan
    ) {
        return new NutrientGoal(
                goalKcal,
                calculateGoalCarbs(goalKcal, nutrientPlan),
                calculateGoalProtein(goalKcal,nutrientPlan),
                calculateGoalFat(goalKcal,nutrientPlan)
        );
    }

    public static NutrientGoal from(
            Member member,
            MemberStatus memberStatus,
            ActivityLevel activityLevel,
            NutrientPlan nutrientPlan
    ) {
        BigDecimal goalKcal = calculateGoalKcal(member, memberStatus, activityLevel);
        return from(goalKcal, nutrientPlan);
    }

    /**
     * 일일 목표 섭취 칼로리 계산
     */
    public static BigDecimal calculateGoalKcal(
            Member member,
            MemberStatus memberStatus,
            ActivityLevel activityLevel
    ){
        double bm = calculateBM(
                member.getAge(),
                member.getGender(),
                memberStatus.getHeight(),
                memberStatus.getWeight()
        );

        double tdee = bm * activityLevel.getValue();

        double dailyAdjustment = 0.0;
        int deltaWeight = memberStatus.getGoalWeight() - memberStatus.getWeight();

        if(Math.abs(deltaWeight) >= 0.5) { // 체중 유지라고 판단 시 오차 0.5 는 허용하도록 함
            dailyAdjustment = (double) (7700 * deltaWeight) / TARGET_DAYS;
        }

        return add(BigDecimal.valueOf(tdee), BigDecimal.valueOf(dailyAdjustment));
    }

    /**
     * 성별에 따른 기초대사량 계산
     */
    private static double calculateBM(
            int age,
            Gender gender,
            int weight,
            int height
    ) {
        return switch (gender) {
            case MALE -> 10 * weight + 6.25 * height - 5 * age + 5;
            case FEMALE -> 10 * weight + 6.25 * height - 5 * age - 161;
        };
    }

    /**
     * 총 섭취 칼로리 x 비율 ÷ 열량
     */
    private static BigDecimal calculateGoalByKcal(
            BigDecimal intakeKcal,
            double ratio,
            BigDecimal kcalPerGram
    ) {
        return divide(
                multiply(intakeKcal, BigDecimal.valueOf(ratio)),
                kcalPerGram
        );
    }

    private static BigDecimal calculateGoalCarbs(
            BigDecimal goalKcal,
            NutrientPlan nutrientPlan
    ) {
        return calculateGoalByKcal(goalKcal, nutrientPlan.getCarbsRatio(), KCAL_PER_GRAM_CARBS);
    }

    private static BigDecimal calculateGoalProtein(
            BigDecimal goalKcal,
            NutrientPlan nutrientPlan
    ) {
        return calculateGoalByKcal(goalKcal, nutrientPlan.getProteinRatio(), KCAL_PER_GRAM_PROTEIN);
    }

    private static BigDecimal calculateGoalFat(
            BigDecimal goalKcal,
            NutrientPlan nutrientPlan
    ){
        return calculateGoalByKcal(goalKcal, nutrientPlan.getFatRatio(), KCAL_PER_GRAM_FAT);
    }

    public BigDecimal getGoalKcal(NutrientCode code) {
        return switch (code){
            case KCAL -> goalKcal;
            case CARBS -> goalCarbs;
            case PROTEIN -> goalProtein;
            case FAT -> goalFat;
            default -> throw new IllegalStateException("Unexpected value: " + code);
        };
    }
}
