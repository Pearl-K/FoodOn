package com.foodon.foodon.intakelog.dto;

import com.foodon.foodon.common.util.BigDecimalUtil;
import com.foodon.foodon.common.util.NutrientGoal;
import com.foodon.foodon.intakelog.domain.IntakeLog;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static com.foodon.foodon.common.util.BigDecimalUtil.round;
import static com.foodon.foodon.common.util.BigDecimalUtil.toRoundedInt;

public record IntakeDetailResponse(
        LocalDate date,
        BigDecimal goalKcal,
        BigDecimal intakeKcal,
        BigDecimal goalCarbs,
        BigDecimal intakeCarbs,
        BigDecimal goalProtein,
        BigDecimal intakeProtein,
        BigDecimal goalFat,
        BigDecimal intakeFat
) {
    public static IntakeDetailResponse withIntakeLog(
            NutrientGoal nutrientGoal,
            IntakeLog intakeLog,
            LocalDate date
    ) {
        return new IntakeDetailResponse(
                intakeLog.getDate(),
                round(intakeLog.getGoalKcal(), 0),
                round(intakeLog.getIntakeKcal(), 0),
                round(nutrientGoal.getGoalCarbs(), 1),
                round(intakeLog.getIntakeCarbs(), 1),
                round(nutrientGoal.getGoalProtein(), 1),
                round(intakeLog.getIntakeProtein(), 1),
                round(nutrientGoal.getGoalFat(), 1),
                round(intakeLog.getIntakeFat(), 1)
        );
    }

    public static IntakeDetailResponse withOutIntakeLog(
            NutrientGoal nutrientGoal,
            LocalDate date
    ) {
        return new IntakeDetailResponse(
                date,
                round(nutrientGoal.getGoalKcal(), 0),
                BigDecimal.ZERO,
                round(nutrientGoal.getGoalCarbs(), 1),
                BigDecimal.ZERO,
                round(nutrientGoal.getGoalProtein(), 1),
                BigDecimal.ZERO,
                round(nutrientGoal.getGoalFat(), 1),
                BigDecimal.ZERO
        );
    }
}
