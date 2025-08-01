package com.foodon.foodon.meal.repository;

import com.foodon.foodon.meal.dto.MealCalendarResponse;
import com.foodon.foodon.meal.dto.MealThumbnailInfo;
import com.foodon.foodon.meal.dto.NutrientIntakeInfo;
import com.foodon.foodon.member.domain.Member;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepositoryCustom {
    List<NutrientIntakeInfo> findNutrientIntakeByMemberAndDate(
            Member member,
            LocalDateTime start,
            LocalDateTime end
    );

    List<MealThumbnailInfo> findRecommendMealsByMemberAndDate(
            Member member,
            LocalDateTime start,
            LocalDateTime end
    );
}
