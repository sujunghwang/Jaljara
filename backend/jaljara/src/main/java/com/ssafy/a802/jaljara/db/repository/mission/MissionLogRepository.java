package com.ssafy.a802.jaljara.db.repository.mission;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.a802.jaljara.db.entity.MissionLog;

public interface MissionLogRepository extends JpaRepository<MissionLog, Long> {

	boolean existsByUserIdAndMissionDate(long userId, Date date);
}
