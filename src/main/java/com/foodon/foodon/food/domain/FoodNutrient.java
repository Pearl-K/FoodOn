package com.foodon.foodon.food.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "food_nutrients",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_food_nutrient", columnNames = {"food_id", "nutrient_id"})
        }
)
public class FoodNutrient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_nutrient_id")
    private Long id;

    @Column(name = "food_id", nullable = false)
    private Long foodId;

    @Column(name = "nutrient_id", nullable = false)
    private Long nutrientId;

    @Column(precision = 7, scale = 2, nullable = false)
    private BigDecimal value;

    private FoodNutrient(
            Long foodId,
            Long nutrientId,
            BigDecimal value
    ) {
        this.foodId = foodId;
        this.nutrientId = nutrientId;
        this.value = value;
    }

    public static FoodNutrient createFoodNutrient(
            Long foodId,
            Long nutrientId,
            BigDecimal value
    ){
        return new FoodNutrient(foodId, nutrientId, value);
    }

}
