package com.foodon.foodon.intakelog.repository;

import com.foodon.foodon.intakelog.domain.IntakeLog;
import com.foodon.foodon.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IntakeLogRepository extends JpaRepository<IntakeLog, Integer> {
    List<IntakeLog> findByMemberAndDateBetween(
            Member member,
            LocalDate startDate,
            LocalDate endDate
    );

    Optional<IntakeLog> findByMemberAndDate(Member member, LocalDate date);
}
