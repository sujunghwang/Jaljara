package com.ssafy.a802.jaljara.db.repository.mission;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ssafy.a802.jaljara.db.entity.Mission;

public interface MissionRepository extends JpaRepository<Mission, Long> {

	@Query(value = "SELECT * FROM mission ORDER BY RAND() LIMIT 1", nativeQuery = true)
	Mission findRandomMission();
}
