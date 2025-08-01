package com.foodon.foodon.food.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.foodon.foodon.food.domain.HealthEffect.UNKNOWN;
import static com.foodon.foodon.food.domain.RestrictionType.NONE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "nutrients")
public class Nutrient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nutrient_id")
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private NutrientCode code;

    @Enumerated(EnumType.STRING)
    private NutrientUnit nutrientUnit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestrictionType restrictionType = NONE;

    private Double restrictionMin;

    private Double restrictionMax;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HealthEffect healthEffect = UNKNOWN;

}
