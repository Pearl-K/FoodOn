package com.foodon.foodon.food.repository;

import com.foodon.foodon.food.dto.FoodSearchCond;
import com.foodon.foodon.food.dto.FoodWithNutrientInfo;
import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.recommend.dto.RecommendedFood;
import com.foodon.foodon.food.dto.NutrientClaimInfo;
import com.foodon.foodon.member.domain.Member;

import java.util.List;
import java.util.Set;

public interface FoodRepositoryCustom {

    FoodWithNutrientInfo findFoodInfoWithNutrientByIdAndType(
            Long foodId,
            FoodType type,
            Member member
    );

    List<FoodWithNutrientInfo> findFoodInfoWithNutrientByNameIn(
            Set<String> foodNames
    );

    List<FoodWithNutrientInfo> findAllFoodInfo();

    List<NutrientClaimInfo> findNutrientClaimsByFoodIds(List<Long> foodIds);

    List<FoodWithNutrientInfo> findAllBySearchCond(FoodSearchCond cond);

    List<RecommendedFood> findRecommendedFoodsWithNutrientInfo();

}
