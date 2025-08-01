package com.foodon.foodon.meal.domain;

import com.foodon.foodon.food.domain.Nutrient;
import com.foodon.foodon.meal.dto.ManageNutrientResponse;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ManageNutrient {

    private final Double min;
    private final Double max;

    private ManageNutrient(Double min, Double max) {
        this.min = min;
        this.max = max;
    }

    public static ManageNutrient from(Nutrient nutrient, BigDecimal intakeKcal) {
        Double min = calculateMin(nutrient, intakeKcal.doubleValue());
        Double max = calculateMax(nutrient, intakeKcal.doubleValue());
        return new ManageNutrient(min, max);
    }

    private static Double calculateMax(Nutrient nutrient, Double intakeKcal) {
        return switch (nutrient.getRestrictionType()) {
            case BY_KCAL -> intakeKcal * nutrient.getRestrictionMax() / 9.0;
            case FIXED -> nutrient.getRestrictionMax();
            default -> null;
        };
    }

    private static Double calculateMin(Nutrient nutrient, Double intakeKcal) {
        return switch (nutrient.getRestrictionType()) {
            case BY_KCAL -> intakeKcal * nutrient.getRestrictionMin() / 9.0;
            case FIXED -> nutrient.getRestrictionMin();
            default -> null;
        };
    }
}
