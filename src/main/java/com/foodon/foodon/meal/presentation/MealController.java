package com.foodon.foodon.meal.presentation;

import com.foodon.foodon.auth.annotation.AuthMember;
import com.foodon.foodon.common.dto.Response;
import com.foodon.foodon.common.util.ResponseUtil;
import com.foodon.foodon.meal.application.MealService;
import com.foodon.foodon.meal.dto.*;
import com.foodon.foodon.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meals")
public class MealController {

    private final MealService mealService;

    @PostMapping(
            value = "/detect",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    @Operation(summary = "식단 사진 업로드하기")
    public ResponseEntity<Response<MealInfoResponse>> uploadAndDetectMeal(
            @RequestPart("image") MultipartFile multipartFile,
            @Parameter(hidden = true) @AuthMember Member member
    ) {
        MealInfoResponse result = mealService.uploadAndDetect(multipartFile);
        return ResponseUtil.success(result);
    }

    @PostMapping
    @Operation(summary = "식단 기록하기")
    public ResponseEntity<Response<Void>> saveMeal(
            @RequestBody MealCreateRequest request,
            @Parameter(hidden = true) @AuthMember Member member
    ) {
        mealService.saveMeal(request, member);
        return ResponseUtil.created();
    }

    @GetMapping("/{date}")
    @Operation(summary = "식단 기록 조회")
    public ResponseEntity<Response<List<MealSummaryResponse>>> getMealSummariesByDate(
            @PathVariable(name = "date") LocalDate date,
            @Parameter(hidden = true) @AuthMember Member member
    ){
        List<MealSummaryResponse> result = mealService.getMealSummariesByDate(date, member);
        return ResponseUtil.success(result);
    }

    @GetMapping("/manage-nutrient/{date}")
    @Operation(summary = "관리 영양소 조회")
    public ResponseEntity<Response<List<ManageNutrientResponse>>> getManageNutrientsByDate(
            @PathVariable(name = "date") LocalDate date,
            @Parameter(hidden = true) @AuthMember Member member
    ){
        List<ManageNutrientResponse> result = mealService.getManageNutrientsByDate(date, member);
        return ResponseUtil.success(result);
    }

    @GetMapping("/detail/{mealId}")
    @Operation(summary = "식단 기록 정보 상세 조회")
    public ResponseEntity<Response<MealDetailInfoResponse>> getMealDetailInfo(
            @PathVariable(name = "mealId") Long mealId,
            @Parameter(hidden = true) @AuthMember Member member
    ){
        MealDetailInfoResponse result = mealService.getMealDetailInfo(mealId, member);
        return ResponseUtil.success(result);
    }

    @GetMapping("/calendar/recommendations")
    @Operation(summary = "달력 추천 식단 조회")
    public ResponseEntity<Response<List<MealCalendarResponse>>> getRecommendMealRecords(
            @RequestParam(name = "yearMonth") YearMonth yearMonth,
            @Parameter(hidden = true) @AuthMember Member member
    ){
        List<MealCalendarResponse> result = mealService.getRecommendMealRecords(yearMonth, member);
        return ResponseUtil.success(result);
    }

}
