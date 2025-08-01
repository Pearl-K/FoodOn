package com.foodon.foodon.recommend.application.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.recommend.application.RecommendFoodService;
import com.foodon.foodon.recommend.dto.RecommendedFood;

import lombok.RequiredArgsConstructor;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendFoodScheduler {

	private final RecommendFoodService recommendFoodService;

	@Scheduled(cron = "0 0 0 * * Mon")
	public void generateWeeklyRecommendationsForAllUsers() {
		log.info("[RecommendFoodScheduler] 추천 음식 생성 스케줄러 시작 {}", LocalDateTime.now());

		List<Member> activeMembers = recommendFoodService.getRecentlyActiveMembers();
		List<RecommendedFood> recommendedFoods = recommendFoodService.loadAllRecommendedFoods();
		LocalDateTime today = LocalDateTime.now();

		if(activeMembers.isEmpty()) {
			log.info("[RecommendFoodScheduler] 추천 음식을 생성할 대상이 없습니다.");
			return;
		}

		for (Member member : activeMembers) {
			recommendFoodService.generateWeeklyRecommendations(
				member,
				recommendedFoods,
				today
			);
		}

		log.info("[RecommendFoodScheduler] 추천 음식 생성 스케줄러 종료 {}명, {}", activeMembers.size(), LocalDateTime.now());
	}

}
