package com.foodon.foodon.meal.domain;

import com.foodon.foodon.meal.dto.PositionInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@Table(name = "positions")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_item_id", nullable = false)
    private MealItem mealItem;

    @Column(nullable = false)
    private double x;

    @Column(nullable = false)
    private double y;

    @Column(nullable = false)
    private double width;

    @Column(nullable = false)
    private double height;

    private double confidence;


    private Position(
            MealItem mealItem,
            double x,
            double y,
            double width,
            double height,
            double confidence
    ) {
        this.mealItem = mealItem;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.confidence = confidence;
    }

    public static Position createPosition(
            MealItem mealItem,
            PositionInfo positionInfo
    ){
        Position position = new Position(
                mealItem,
                positionInfo.x(),
                positionInfo.y(),
                positionInfo.width(),
                positionInfo.height(),
                positionInfo.confidence()
        );

        mealItem.addPosition(position);
        return position;
    }
}
