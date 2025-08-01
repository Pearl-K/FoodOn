package com.foodon.foodon.member.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.foodon.foodon.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

		@Query("SELECT m FROM Member m WHERE m.lastLoginAt >= :startDate")
		List<Member> findAllActiveMembers(@Param("startDate") LocalDateTime startDate);

}
