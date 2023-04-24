package com.ssafy.a802.jaljara.db.repository;

import com.ssafy.a802.jaljara.db.entity.SleepLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SleepLogRepository extends JpaRepository<SleepLog, Long> {
    SleepLog findByUserIdAndDate(long userId, LocalDate date);
    List<SleepLog> findAllByUserIdAndDateBetweenOrderByDate(long userId, LocalDate before, LocalDate after);
//    where year([column_name]) = year(now())
//    and month([column_name]) = month(now())
}
