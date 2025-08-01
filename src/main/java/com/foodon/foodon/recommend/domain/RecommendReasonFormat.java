package com.foodon.foodon.recommend.domain;

import lombok.Getter;

@Getter
public enum RecommendReasonFormat {

    // 부족한 영양소 보충
    PROTEIN_DEFICIENT("단백질이 부족했어요. 단백질 보충에 도움이 돼요"),
    CARBS_DEFICIENT("탄수화물이 부족했어요. 탄수화물 보충에 적합해요"),
    FAT_DEFICIENT("지방 보충에 유용해요"),
    KCAL_DEFICIENT("열량이 적었어요. 열량 보충에 도움 돼요!"),

    // 과다한 영양소, 적은 음식
    LOW_FAT("지방 비율이 높았어요. 지방이 적은 음식이에요"),
    LOW_CARBS("탄수화물이 과다했어요. 탄수화물이 적어 가볍게 먹기 좋아요"),
    LOW_KCAL("열량이 높았어요. 열량이 낮은 음식을 추천해드릴게요!"),

    // 균형
    BALANCED("탄단지 비율이 완벽한 음식이에요!")
    ;

    private final String message;

    RecommendReasonFormat(String message) {
        this.message = message;
    }
}
