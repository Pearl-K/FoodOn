package com.foodon.foodon.member.repository;

import com.foodon.foodon.member.domain.ActivityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLevelRepository extends JpaRepository<ActivityLevel, Long> {
}
