package com.foodon.foodon.food.repository;

import com.foodon.foodon.food.domain.Nutrient;
import com.foodon.foodon.food.domain.RestrictionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NutrientRepository extends JpaRepository<Nutrient, Long> {
    @Query("SELECT n.id FROM Nutrient n WHERE n.id IN :ids")
    List<Long> findExistingIds(@Param("ids") List<Long> ids);

    List<Nutrient> findByRestrictionTypeIsNot(RestrictionType restrictionType);
}
