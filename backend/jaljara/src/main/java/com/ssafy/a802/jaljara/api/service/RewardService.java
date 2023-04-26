package com.ssafy.a802.jaljara.api.service;

import com.ssafy.a802.jaljara.api.dto.response.RewardResponseDto;
import com.ssafy.a802.jaljara.db.entity.ChildInformation;
import com.ssafy.a802.jaljara.db.entity.Reward;
import com.ssafy.a802.jaljara.db.entity.User;
import com.ssafy.a802.jaljara.db.entity.UserType;
import com.ssafy.a802.jaljara.db.repository.ChildInformationRepository;
import com.ssafy.a802.jaljara.db.repository.RewardRepository;
import com.ssafy.a802.jaljara.db.repository.UserRepository;
import com.ssafy.a802.jaljara.exception.CustomException;
import com.ssafy.a802.jaljara.exception.ExceptionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardService {
    private final UserRepository userRepository;
    private final RewardRepository rewardRepository;
    private final ChildInformationRepository childInformationRepository;

    public void addReward(long childId){
        //childId에 해당하는 유저가 없는 경우
        User child = userRepository.findById(childId).orElseThrow(
                () -> ExceptionFactory.userNotFound(childId));

        //해당 유저가 자녀 유저가 아닌 경우
        if(child.getUserType() != UserType.CHILD)
            throw ExceptionFactory.userTypeMismatch(childId, UserType.CHILD);

        //해당 유저와 연결된 부모가 없는 경우
        ChildInformation childInformation = childInformationRepository.findByChildId(childId).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "부모와 연결되지 않은 유저입니다. userId: " + childId));

        //등록된 보상이 없는 경우
        if(childInformation.getCurrentReward().equals(""))
            throw new CustomException(HttpStatus.NOT_FOUND, "등록된 보상이 없습니다.");

        rewardRepository.save(Reward.builder()
                .userId(childId)
                .content(childInformation.getCurrentReward())
                .build());
        childInformationRepository.save(childInformation.toBuilder()
                .currentReward("")
                .streakCount(0)
                .build());
    }

    public List<RewardResponseDto.RewardUsable> findUsableRewardListByUserId(long childId){
        //childId에 해당하는 유저가 없는 경우
        User child = userRepository.findById(childId).orElseThrow(
                () -> ExceptionFactory.userNotFound(childId));

        //해당 유저가 자녀 유저가 아닌 경우
        if(child.getUserType() != UserType.CHILD)
            throw ExceptionFactory.userTypeMismatch(childId, UserType.CHILD);

        //사용전인 보상 리스트를 Dto에 담아서 return
        List<Reward> rewards = rewardRepository.findAllByUserIdAndIsUsed(childId, false);
        List<RewardResponseDto.RewardUsable> usableRewards = new ArrayList<>();
        for(Reward reward : rewards){
            usableRewards.add(RewardResponseDto.RewardUsable.builder()
                            .rewardId(reward.getId())
                            .userId(childId)
                            .content(reward.getContent())
                            .getTime(reward.getCreatedAt())
                            .build());
        }

        return usableRewards;
    }

    public List<RewardResponseDto.RewardUsed> findUsedRewardListByUserId(long childId){
        //childId에 해당하는 유저가 없는 경우
        User child = userRepository.findById(childId).orElseThrow(
                () -> ExceptionFactory.userNotFound(childId));

        //해당 유저가 자녀 유저가 아닌 경우
        if(child.getUserType() != UserType.CHILD)
            throw ExceptionFactory.userTypeMismatch(childId, UserType.CHILD);

        //사용한 보상 리스트를 Dto에 담아서 return
        List<Reward> rewards = rewardRepository.findAllByUserIdAndIsUsed(childId, true);
        List<RewardResponseDto.RewardUsed> usedRewards = new ArrayList<>();
        for(Reward reward : rewards){
            usedRewards.add(RewardResponseDto.RewardUsed.builder()
                            .rewardId(reward.getId())
                            .userId(childId)
                            .content(reward.getContent())
                            .getTime(reward.getCreatedAt())
                            .usedTime(reward.getUpdatedAt())
                            .build());
        }

        return usedRewards;
    }

    public void modifyRewardUsed(long rewardId){
        //rewardId에 해당하는 보상이 존재하지 않는 경우
        Reward reward = rewardRepository.findById(rewardId).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "해당 보상이 존재하지 않습니다. rewardId: " + rewardId));

        //해당 보상이 이미 사용된 경우
        if(reward.isUsed())
            throw new CustomException(HttpStatus.BAD_REQUEST, "해당 보상은 이미 사용되었습니다. 보상: " + reward.getContent());

        rewardRepository.save(reward.toBuilder().isUsed(true).build());
    }
}
