package com.ssafy.a802.jaljara.db.repository;

import com.ssafy.a802.jaljara.db.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long> {
    List<Reward> findAllByUserIdAndIsUsedOrderByUpdatedAtDesc(long userId, boolean isUsed);
}
