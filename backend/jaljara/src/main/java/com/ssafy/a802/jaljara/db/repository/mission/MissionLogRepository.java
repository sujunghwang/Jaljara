package com.ssafy.a802.jaljara.db.repository.mission;

import com.ssafy.a802.jaljara.db.entity.MissionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MissionLogRepository extends JpaRepository<MissionLog, Long> {
	boolean existsByUserIdAndMissionDate(long userId, Date date);
    List<MissionLog> findAllByUserIdAndMissionDateBetweenAndIsSuccess(long userId, Date before, Date after, boolean isSuccess);
    Optional<MissionLog> findByUserIdAndMissionDate(long userId, Date date);
}
