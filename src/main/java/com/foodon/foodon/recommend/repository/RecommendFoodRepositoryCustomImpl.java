package com.foodon.foodon.recommend.repository;

import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.recommend.domain.QRecommendFood;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.foodon.foodon.recommend.domain.QRecommendFood.recommendFood;

@Repository
@RequiredArgsConstructor
public class RecommendFoodRepositoryCustomImpl implements RecommendFoodRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsThisWeekRecommend(
            Member member,
            FoodType foodType,
            Long foodId
    ) {
        LocalDateTime startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeek = startOfWeek.plusWeeks(1);

        Integer fetchOne = queryFactory
                .selectOne()
                .from(recommendFood)
                .where(
                        recommendFood.member.eq(member),
                        recommendFood.foodType.eq(foodType),
                        recommendFood.foodId.eq(foodId),
                        recommendFood.createdAt.between(startOfWeek, endOfWeek)
                )
                .fetchFirst();

        return fetchOne != null;
    }
}
