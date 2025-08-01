package com.foodon.foodon.recommend.repository;

import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.member.domain.Member;

public interface RecommendFoodRepositoryCustom {
    boolean existsThisWeekRecommend(
            Member member,
            FoodType foodType,
            Long foodId
    );
}
