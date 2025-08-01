package com.foodon.foodon.intakelog.application;

import com.foodon.foodon.intakelog.dto.IntakeSummaryResponse;
import com.foodon.foodon.intakelog.infrastructure.cache.IntakeLogCacheManager;
import com.foodon.foodon.intakelog.repository.IntakeLogRepository;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.member.repository.ActivityLevelRepository;
import com.foodon.foodon.member.repository.MemberStatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class IntakeLogServiceTest {

    @Mock
    private IntakeLogRepository intakeLogRepository;

    @Mock
    private ActivityLevelRepository activityLevelRepository;

    @Mock
    private MemberStatusRepository memberStatusRepository;

    @Mock
    private IntakeLogCacheManager cacheManager;

    @InjectMocks
    private IntakeLogService intakeLogService;

    @Test
    void 캐시가_일부만_존재할_때_부족한_날짜만_계산하여_응답을_완성한다() {
        // Given
        YearMonth yyMm = YearMonth.of(2025, 7);
        LocalDate d1 = yyMm.atDay(1);
        LocalDate d2 = yyMm.atDay(2);
        LocalDate d3 = yyMm.atDay(3);

        Member member = Mockito.mock(Member.class);
        given(member.getId()).willReturn(1L);
        given(member.getCreatedAt()).willReturn(d1.atStartOfDay());

        Map<LocalDate, IntakeSummaryResponse> cached = Map.of(
                d1, IntakeSummaryResponse.withoutIntakeLog(BigDecimal.TEN, d1)
        );

        given(cacheManager.getCachedMonth("1", yyMm)).willReturn(cached);
        given(memberStatusRepository.findByMemberIdAndCreatedAtBetweenOrderByCreatedAt(any(), any(), any()))
                .willReturn(List.of());
        given(activityLevelRepository.findAll()).willReturn(List.of());
        given(intakeLogRepository.findByMemberAndDateBetween(eq(member), any(), any()))
                .willReturn(List.of());

        // When
        List<IntakeSummaryResponse> result = intakeLogService.getIntakeLogCalendar(yyMm, member);

        // Then
        assertEquals(yyMm.lengthOfMonth(), result.size());
        assertTrue(result.stream().anyMatch(r -> r.date().equals(d1)));
        assertTrue(result.stream().anyMatch(r -> r.date().equals(d2)));
        assertTrue(result.stream().anyMatch(r -> r.date().equals(d3)));
    }
}
