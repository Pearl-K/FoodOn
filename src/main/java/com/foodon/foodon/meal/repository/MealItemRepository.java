package com.foodon.foodon.meal.repository;

import com.foodon.foodon.meal.domain.MealItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealItemRepository extends JpaRepository<MealItem, Long> {
}
