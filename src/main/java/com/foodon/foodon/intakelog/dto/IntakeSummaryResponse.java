package com.foodon.foodon.intakelog.dto;

import com.foodon.foodon.intakelog.domain.IntakeLog;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.foodon.foodon.common.util.BigDecimalUtil.round;
import static com.foodon.foodon.common.util.BigDecimalUtil.toRoundedInt;


public record IntakeSummaryResponse(
        LocalDate date,
        BigDecimal goalKcal,
        BigDecimal intakeKcal
) {
    public static IntakeSummaryResponse withIntakeLog(IntakeLog intakeLog) {
        return new IntakeSummaryResponse(
                intakeLog.getDate(),
                round(intakeLog.getGoalKcal(), 0),
                round(intakeLog.getIntakeKcal(), 0)
        );
    }

    public static IntakeSummaryResponse withoutIntakeLog(
            BigDecimal goalKcal,
            LocalDate date
    ) {
        return new IntakeSummaryResponse(
                date,
                round(goalKcal, 0),
                BigDecimal.ZERO
        );
    }
}
