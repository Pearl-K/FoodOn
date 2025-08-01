package com.foodon.foodon.intakelog.presentation;

import com.foodon.foodon.auth.annotation.AuthMember;
import com.foodon.foodon.common.dto.Response;
import com.foodon.foodon.common.util.ResponseUtil;
import com.foodon.foodon.intakelog.application.IntakeLogService;
import com.foodon.foodon.intakelog.dto.IntakeDetailResponse;
import com.foodon.foodon.intakelog.dto.IntakeSummaryResponse;
import com.foodon.foodon.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/intake")
public class IntakeLogController {

    private final IntakeLogService intakeLogService;

    @GetMapping("/calendar")
    @Operation(summary = "달력 섭취 기록 조회")
    public ResponseEntity<Response<List<IntakeSummaryResponse>>> getIntakeLogCalendar(
            @RequestParam(name = "yearMonth") YearMonth yearMonth,
            @Parameter(hidden = true) @AuthMember Member member
    ) {
        List<IntakeSummaryResponse> result = intakeLogService.getIntakeLogCalendar(yearMonth, member);
        return ResponseUtil.success(result);
    }

    @GetMapping("/{date}")
    @Operation(summary = "오늘 섭취량, 탄단지 정보 조회")
    public ResponseEntity<Response<IntakeDetailResponse>> getDailyIntakeDetail(
            @PathVariable(name = "date") LocalDate date,
            @Parameter(hidden = true) @AuthMember Member member
    ) {
        IntakeDetailResponse result = intakeLogService.getIntakeDailyDetail(date, member);
        return ResponseUtil.success(result);
    }

    @GetMapping("/target/{date}")
    public ResponseEntity<Response<IntakeSummaryResponse>> getIntakeLogByTargetDate(
        @PathVariable(name = "date") LocalDate date,
        @Parameter(hidden = true) @AuthMember Member member
    ) {
        IntakeSummaryResponse result = intakeLogService.getIntakeLogByTargetDate(date, member);
        return ResponseUtil.success(result);
    }
}
