package com.ssafy.a802.jaljara.db.repository;

import com.ssafy.a802.jaljara.db.entity.SleepLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface SleepLogRepository extends JpaRepository<SleepLog, Long> {
    Optional<SleepLog> findByUserIdAndDate(long userId, Date date);
}
