package com.foodon.foodon.recommend.repository;

import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.recommend.domain.RecommendFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface RecommendFoodRepository extends JpaRepository<RecommendFood, Long>, RecommendFoodRepositoryCustom {
    List<RecommendFood> findByMemberAndCreatedAtBetween(
            Member member,
            LocalDateTime start,
            LocalDateTime end
    );
}
