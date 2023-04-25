package com.ssafy.a802.jaljara.api.service;

import com.ssafy.a802.jaljara.api.dto.response.RewardResponseDto;
import com.ssafy.a802.jaljara.db.entity.ChildInformation;
import com.ssafy.a802.jaljara.db.entity.Reward;
import com.ssafy.a802.jaljara.db.repository.ChildInformationRepository;
import com.ssafy.a802.jaljara.db.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardService {
    private final RewardRepository rewardRepository;
    private final ChildInformationRepository childInformationRepository;

    public List<RewardResponseDto.RewardUsable> findUsableRewardListByUserId(long userId){
        List<Reward> rewards = rewardRepository.findAllByUserIdAndIsUsed(userId, false);
        List<RewardResponseDto.RewardUsable> usableRewards = new ArrayList<>();
        for(Reward reward : rewards){
            usableRewards.add(RewardResponseDto.RewardUsable.builder()
                            .rewardId(reward.getId())
                            .userId(userId)
                            .content(reward.getContent())
                            .getTime(reward.getCreatedAt())
                            .build());
        }

        return usableRewards;
    }

    public List<RewardResponseDto.RewardUsed> findUsedRewardListByUserId(long userId){
        List<Reward> rewards = rewardRepository.findAllByUserIdAndIsUsed(userId, true);
        List<RewardResponseDto.RewardUsed> usedRewards = new ArrayList<>();
        for(Reward reward : rewards){
            usedRewards.add(RewardResponseDto.RewardUsed.builder()
                            .rewardId(reward.getId())
                            .userId(userId)
                            .content(reward.getContent())
                            .getTime(reward.getCreatedAt())
                            .usedTime(reward.getUpdatedAt())
                            .build());
        }

        return usedRewards;
    }

    public void modifyRewardUsed(long rewardId){
        Reward reward = rewardRepository.findById(rewardId).orElseThrow(
                () -> new IllegalArgumentException("해당 보상이 존재하지 않습니다. userId: " + rewardId));
        rewardRepository.save(reward.toBuilder().isUsed(true).build());
    }

    public void addReward(long childId){
        ChildInformation childInformation = childInformationRepository.findByChildId(childId);
        rewardRepository.save(Reward.builder()
                .userId(childId)
                .content(childInformation.getCurrentReward())
                .build());
        childInformationRepository.save(childInformation.toBuilder()
                .currentReward("")
                .streakCount(0)
                .build());
    }
}
