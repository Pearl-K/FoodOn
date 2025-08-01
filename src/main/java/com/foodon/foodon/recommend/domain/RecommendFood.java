package com.foodon.foodon.recommend.domain;

import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.recommend.dto.RecommendedFood;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "recommend_foods",
        indexes = {
                @Index(name = "idx_member_created", columnList = "member_id, created_at"),
                @Index(name = "idx_member_type_id_created", columnList = "member_id, food_type, food_id, created_at")
        }
)
public class RecommendFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommend_food_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodType foodType;

    @Column(nullable = false)
    private Long foodId;

    @Column(nullable = false)
    private String foodName;

    @Column(precision = 7, scale = 2, nullable = false)
    private BigDecimal kcalPerServing; // 1회 제공 열량 (100g 당 x)

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(length = 100)
    private String reason;


    private RecommendFood(
            Member member,
            FoodType foodType,
            Long foodId,
            String foodName,
            BigDecimal kcalPerServing,
            LocalDateTime createdAt,
            String reason
    ){
        this.member = member;
        this.foodType = foodType;
        this.foodId = foodId;
        this.foodName = foodName;
        this.kcalPerServing = kcalPerServing;
        this.createdAt = createdAt;
        this.reason = reason;
    }

    public static RecommendFood from(
        RecommendedFood recommendedFoodCache,
        Member member,
        String reason,
        LocalDateTime createdAt
    ) {
        return new RecommendFood(
            member,
            FoodType.PUBLIC,
            recommendedFoodCache.foodId(),
            recommendedFoodCache.foodName(),
            recommendedFoodCache.kcal(),
            createdAt,
            reason
        );
    }
}


