package com.foodon.foodon.meal.domain;

import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.meal.dto.MealItemInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "meal_items")
public class MealItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @Enumerated(EnumType.STRING)
    private FoodType foodType;

    private Long foodId;

    private String foodName;

    @Column(precision = 2, scale = 1)
    private BigDecimal quantity;

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "mealItem", cascade = CascadeType.PERSIST)
    private List<Position> positions = new ArrayList<>();

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean isDeleted = false;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean isRecommended = false;

    private MealItem(
            Meal meal,
            FoodType foodType,
            Long foodId,
            String foodName,
            BigDecimal quantity,
            boolean isRecommended
    ) {
        this.meal = meal;
        this.foodType = foodType;
        this.foodId = foodId;
        this.foodName = foodName;
        this.quantity = quantity;
        this.isRecommended = isRecommended;
    }

    public static MealItem createMealItem(
            Meal meal,
            MealItemInfo mealItemInfo,
            boolean isRecommended
    ) {
        MealItem mealItem = new MealItem(
                meal,
                mealItemInfo.type(),
                mealItemInfo.foodId(),
                mealItemInfo.foodName(),
                mealItemInfo.quantity(),
                isRecommended
        );

        meal.addMealItem(mealItem);
        return mealItem;
    }

    public void addPosition(Position position) {
        this.positions.add(position);
    }

}
