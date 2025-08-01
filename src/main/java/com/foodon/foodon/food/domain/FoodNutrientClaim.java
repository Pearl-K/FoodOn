package com.foodon.foodon.food.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "food_nutrient_claims",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_food_nutrient_claim", columnNames = {"food_id", "nutrient_claim_id"})
        }
)
public class FoodNutrientClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_nutrient_claim_id")
    private Long id;

    @Column(nullable = false)
    private Long foodId;

    @Column(nullable = false)
    private Long nutrientClaimId;


    private FoodNutrientClaim(
            Long foodId,
            Long nutrientClaimId
    ){
        this.foodId = foodId;
        this.nutrientClaimId = nutrientClaimId;
    }

    public static FoodNutrientClaim createNutrientClaimOfFood(
            Long foodId,
            Long nutrientClaimId
    ){
        return new FoodNutrientClaim(foodId, nutrientClaimId);
    }

}
