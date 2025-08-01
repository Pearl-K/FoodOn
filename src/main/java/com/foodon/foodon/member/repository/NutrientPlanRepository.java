package com.foodon.foodon.member.repository;

import com.foodon.foodon.member.domain.NutrientPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutrientPlanRepository extends JpaRepository<NutrientPlan, Long> {
}
