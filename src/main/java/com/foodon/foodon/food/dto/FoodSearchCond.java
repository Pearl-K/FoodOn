package com.foodon.foodon.food.dto;

import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class FoodSearchCond {
    private FoodType foodType;
    private List<Long> foodIds;
    private Member member;

    @Builder
    private FoodSearchCond(Member member, FoodType foodType, List<Long> foodIds) {
        this.member = member;
        this.foodType = foodType;
        this.foodIds = foodIds;
    }

    public static FoodSearchCond of(List<Long> foodIds) {
        return FoodSearchCond.builder()
                .foodIds(foodIds)
                .foodType(FoodType.PUBLIC)
                .build();
    }

    public static FoodSearchCond of(List<Long> foodIds, Member member) {
        return FoodSearchCond.builder()
                .foodIds(foodIds)
                .foodType(FoodType.CUSTOM)
                .member(member)
                .build();
    }
}
