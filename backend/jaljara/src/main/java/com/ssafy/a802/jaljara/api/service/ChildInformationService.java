package com.ssafy.a802.jaljara.api.service;

import com.ssafy.a802.jaljara.api.dto.request.ChildInformationRequestDto;
import com.ssafy.a802.jaljara.api.dto.response.ChildInformationResponseDto;
import com.ssafy.a802.jaljara.api.dto.response.UserResponseDto;
import com.ssafy.a802.jaljara.db.entity.ChildInformation;
import com.ssafy.a802.jaljara.db.repository.ChildInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildInformationService {
    private final ChildInformationRepository childInformationRepository;

    public void addChildInformation(long parentId, long childId){
        childInformationRepository.save(ChildInformation.builder()
                .childId(childId)
                .parentId(parentId)
                .build());
    }

    public void modifyCurrentReward(ChildInformationRequestDto.CurrentRewardInput currentRewardInput){
        ChildInformation childInformation = childInformationRepository.findByChildId(currentRewardInput.getChildId());
        childInformationRepository.save(
                childInformation.toBuilder()
                        .currentReward(currentRewardInput.getReward())
                        .build());
    }

    public void modifyTargetSleepTime(ChildInformationRequestDto.TargetSleepInput targetSleepInput){
        ChildInformation childInformation = childInformationRepository.findByChildId(targetSleepInput.getChildId());
        childInformationRepository.save(
                childInformation.toBuilder()
                        .targetBedTime(targetSleepInput.getTargetBedTime())
                        .targetWakeupTime(targetSleepInput.getTargetWakeupTime())
                        .build());
    }

    public List<UserResponseDto.SimpleUserInfo> findChildListByParentId(long parentId){
        List<ChildInformation> childInformations = childInformationRepository.findAllByParentId(parentId);
        List<UserResponseDto.SimpleUserInfo> simpleChildInfoList = new ArrayList<>();
        for(ChildInformation childInformation : childInformations){
            simpleChildInfoList.add(UserResponseDto.SimpleUserInfo.builder()
                    .userId(childInformation.getChildId())
                    .profileImageUrl(childInformation.getChild().getProfileImageUrl())
                    .build());
        }
        return simpleChildInfoList;
    }

    public ChildInformationResponseDto.ChildInfoDetail findChildInformationByChildId(long childId){
        ChildInformation childInformation = childInformationRepository.findByChildId(childId);
        return ChildInformationResponseDto.ChildInfoDetail.builder()
                .childId(childId)
                .currentReward(childInformation.getCurrentReward())
                .streakCount(childInformation.getStreakCount())
                .targetBedTime(childInformation.getTargetBedTime())
                .targetWakeupTime(childInformation.getTargetWakeupTime())
                .build();
    }

    public void removeChildInformationByChildId(long childId){
        //부모 - 자녀 관계만 삭제함 (child의 유저 정보도 삭제 해야 하나?)
        childInformationRepository.deleteByChildId(childId);
    }
}
