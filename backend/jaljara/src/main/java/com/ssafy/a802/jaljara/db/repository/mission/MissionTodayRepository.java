package com.ssafy.a802.jaljara.db.repository.mission;

import com.ssafy.a802.jaljara.db.entity.MissionToday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionTodayRepository extends JpaRepository<MissionToday, Long> {
	Optional<MissionToday> findByUserId(Long userId);
}
