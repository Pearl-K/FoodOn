package com.foodon.foodon.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "nutrient_plans")
public class NutrientPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nutrient_plan_id")
    private Long id;

    @Column(nullable = false, name = "nutrient_plan_name", length = 50)
    private String name;

    @Column(name = "nutrient_plan_description", length = 200)
    private String description;

    @Column(nullable = false)
    private double carbsRatio;

    @Column(nullable = false)
    private double proteinRatio;

    @Column(nullable = false)
    private double fatRatio;
}
