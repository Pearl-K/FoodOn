package com.foodon.foodon.food.repository;

import com.foodon.foodon.food.domain.FoodNutrientClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodNutrientClaimRepository extends JpaRepository<FoodNutrientClaim, Long> {
}
