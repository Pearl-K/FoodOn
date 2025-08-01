package com.foodon.foodon.meal.repository;

import com.foodon.foodon.meal.domain.Meal;
import com.foodon.foodon.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long>, MealRepositoryCustom {

    @Query("""
        select m from Meal m
        join fetch m.mealItems mi
        where m.member = :member
        and m.mealTime between :start and :end
    """)
    List<Meal> findByMemberAndMealTimeBetween(
            @Param("member") Member member,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
        select m from Meal m
        join fetch m.mealItems mi
        where m.member = :member
        and m.id = :mealId
    """)
    Optional<Meal> findByMealIdFetchWithMealItems(
            @Param("member") Member member,
            @Param("mealId") Long mealId
    );
}
