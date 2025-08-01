package com.foodon.foodon.meal.domain;

import com.foodon.foodon.meal.dto.MealCreateRequest;
import com.foodon.foodon.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalTime.parse;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "meals", indexes = {
        @Index(name = "idx_meal_member_mealtime", columnList = "member_id, meal_time")
})
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private MealTimeType mealTimeType;

    @Column(nullable = false)
    private LocalDateTime mealTime;

    @Column(precision = 7, scale = 2, nullable = false)
    private BigDecimal totalKcal;

    @Column(precision = 7, scale = 2, nullable = false)
    private BigDecimal totalCarbs;

    @Column(precision = 7, scale = 2, nullable = false)
    private BigDecimal totalProtein;

    @Column(precision = 7, scale = 2, nullable = false)
    private BigDecimal totalFat;

    private String mealImage;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.PERSIST)
    private List<MealItem> mealItems = new ArrayList<>();

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean isDeleted = false;

    private Meal(
            Member member,
            MealTimeType mealTimeType,
            BigDecimal totalKcal,
            BigDecimal totalCarbs,
            BigDecimal totalProtein,
            BigDecimal totalFat,
            LocalDateTime mealTime,
            String mealImage
    ) {
        this.member = member;
        this.mealTimeType = mealTimeType;
        this.totalKcal = totalKcal;
        this.totalCarbs = totalCarbs;
        this.totalProtein = totalProtein;
        this.totalFat = totalFat;
        this.mealTime = mealTime;
        this.mealImage = mealImage;
    }

    public static Meal createMeal(
            Member member,
            String imageUrl,
            MealCreateRequest request
    ) {
        return new Meal(
                member,
                request.mealTimeType(),
                request.totalKcal(),
                request.totalCarbs(),
                request.totalProtein(),
                request.totalFat(),
                LocalDate.now().atTime(parse(request.mealTime())),
                imageUrl
        );
    }

    public void addMealItem(MealItem mealItem) {
        this.mealItems.add(mealItem);
    }
}
