package com.foodon.foodon.food.domain;

import com.foodon.foodon.food.dto.CustomFoodCreateRequest;
import com.foodon.foodon.meal.domain.MealItem;
import com.foodon.foodon.meal.dto.MealItemInfo;
import com.foodon.foodon.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigDecimal;

import static com.foodon.foodon.food.domain.FoodType.PUBLIC;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "foods",
        indexes = {
                @Index(name = "idx_food_name", columnList = "food_name"),
                @Index(name = "idx_member_food_id", columnList = "member_id, food_id"),
                @Index(name = "idx_food_member_name", columnList = "member_id, food_name")
        }
)
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long id;

    private Long memberId; // 커스텀

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodType foodType = PUBLIC;

    @Column(name = "food_name", unique = true, nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String displayName;

    @Column(length = 100)
    private String category;

    @Column(precision = 7, scale = 2)
    private BigDecimal servingSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unit unit;

    @Column(nullable = false)
    private boolean searchable;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isRecommended;

    private Food(
            Long memberId,
            FoodType foodType,
            String name,
            String displayName,
            BigDecimal servingSize,
            Unit unit,
            boolean searchable
    ){
        this.memberId = memberId;
        this.foodType = foodType;
        this.name = name;
        this.displayName = displayName;
        this.servingSize = servingSize;
        this.unit = unit;
        this.searchable = searchable;
    }

    public static Food createCustomFoodByMember(
            CustomFoodCreateRequest request,
            Member member
    ) {
        return new Food(
                member.getId(),
                FoodType.CUSTOM,
                request.foodName().trim(),
                request.foodName().trim(),
                request.servingSize(),
                request.unit(),
                true
        );
    }

    public static Food createCustomFoodModifiedByMember(
            CustomFoodCreateRequest request,
            String registerName,
            Member member
    ){
        return new Food(
                member.getId(),
                FoodType.CUSTOM_MODIFIED,
                registerName,
                request.foodName().trim(),
                request.servingSize(),
                request.unit(),
                false
        );
    }

}