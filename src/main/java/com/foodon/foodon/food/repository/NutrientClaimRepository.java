package com.foodon.foodon.food.repository;

import com.foodon.foodon.food.domain.FoodNutrientClaim;
import com.foodon.foodon.food.domain.NutrientClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NutrientClaimRepository extends JpaRepository<NutrientClaim, Long> {
}
