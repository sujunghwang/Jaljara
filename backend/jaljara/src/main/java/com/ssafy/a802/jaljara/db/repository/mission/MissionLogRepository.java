package com.ssafy.a802.jaljara.db.repository.mission;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.a802.jaljara.db.entity.MissionLog;

import java.util.Date;
import java.util.List;

public interface MissionLogRepository extends JpaRepository<MissionLog, Long> {
    List<MissionLog> findAllByUserIdAndMissionDateBetween(long userId, Date before, Date after);
}
