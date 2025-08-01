package com.foodon.foodon.food.repository;

import com.foodon.foodon.food.domain.FoodNutrient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodNutrientRepository extends JpaRepository<FoodNutrient, Long> {

}
