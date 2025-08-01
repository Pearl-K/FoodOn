package com.foodon.foodon.intakelog.infrastructure.cache;

import com.foodon.foodon.intakelog.dto.IntakeSummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class IntakeLogCacheManager {

    private static final String CALENDAR_INTAKE_KEY_PREFIX = "calendar:intake:%s:%s";
    private static final Duration CALENDAR_TTL = Duration.ofDays(7);

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 월 단위 섭취 요약 데이터를 Redis에서 조회합니다.
     * 조회된 키는 즉시 TTL을 갱신하여 자주 조회되는 월은 더 오래 캐시되도록 유지합니다.
     *
     * @param userId 사용자 ID
     * @param yyMm 조회 대상 연월 (예: 2025-08)
     * @return 날짜별 {@link IntakeSummaryResponse} mapping
     */
    public Map<LocalDate, IntakeSummaryResponse> getCachedMonth(
            String userId,
            YearMonth yyMm
    ) {
        String cacheKey = buildKey(userId, yyMm);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(cacheKey);

        Map<LocalDate, IntakeSummaryResponse> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            try {
                LocalDate date = LocalDate.parse(entry.getKey().toString());
                IntakeSummaryResponse value = (IntakeSummaryResponse) entry.getValue();
                result.put(date, value);
            } catch (Exception e) {
                log.error("Failed to deserialize cache value. key = {}, field = {}", cacheKey, entry.getKey(), e);
            }
        }

        redisTemplate.expire(cacheKey, CALENDAR_TTL);
        return result;
    }

    /**
     * 단일 날짜에 해당하는 섭취 요약 데이터를 Redis에 저장합니다.
     * 동일 월 단위 캐시 키에 HSET 형태로 추가되며, TTL이 갱신됩니다.
     *
     * @param userId 사용자 ID
     * @param yearMonth 저장 대상 연월 (예: 2025-08)
     * @param date 저장할 날짜 (예: 2025-08-01)
     * @param value {@link IntakeSummaryResponse} 객체
     */
    public void putDay(
            String userId,
            YearMonth yearMonth,
            LocalDate date,
            IntakeSummaryResponse value
    ) {
        String key = buildKey(userId, yearMonth);
        String field = date.toString();

        redisTemplate.opsForHash().put(key, field, value);
        redisTemplate.expire(key, CALENDAR_TTL);
    }

    private String buildKey(String userId, YearMonth yearMonth) {
        return String.format(CALENDAR_INTAKE_KEY_PREFIX, userId, yearMonth.toString());
    }
}
