package com.ssafy.a802.jaljara.api.service;

import com.ssafy.a802.jaljara.api.dto.request.ChildInformationRequestDto;
import com.ssafy.a802.jaljara.api.dto.response.ChildInformationResponseDto;
import com.ssafy.a802.jaljara.api.dto.response.UserResponseDto;
import com.ssafy.a802.jaljara.db.entity.UserType;
import com.ssafy.a802.jaljara.exception.CustomException;
import com.ssafy.a802.jaljara.db.entity.ChildInformation;
import com.ssafy.a802.jaljara.db.entity.User;
import com.ssafy.a802.jaljara.db.repository.ChildInformationRepository;
import com.ssafy.a802.jaljara.db.repository.UserRepository;
import com.ssafy.a802.jaljara.exception.ExceptionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildInformationService {
    private final ChildInformationRepository childInformationRepository;
    private final UserRepository userRepository;

    public void addChildInformation(long parentId, long childId){
        //id에 해당하는 유저가 없는 경우
        User parent = userRepository.findById(parentId).orElseThrow(
                () -> ExceptionFactory.userNotFound(parentId));
        User child = userRepository.findById(childId).orElseThrow(
                () -> ExceptionFactory.userNotFound(childId));

        //해당 유저의 유저 타입이 다른 경우
        if(parent.getUserType() != UserType.PARENTS)
            throw ExceptionFactory.userTypeMismatch(parentId, UserType.PARENTS);
        if(child.getUserType() != UserType.CHILD)
            throw ExceptionFactory.userTypeMismatch(childId, UserType.CHILD);

        //이미 자녀로 등록된 유저인 경우
        if(childInformationRepository.existsByChildId(childId))
            throw new CustomException(HttpStatus.CONFLICT, "이미 자녀로 등록된 유저입니다. userId: " + childId);

        //새로운 부모-자녀 관계 생성
        childInformationRepository.save(ChildInformation.builder()
                .child(child)
                .parent(parent)
                .build());
    }

    public List<UserResponseDto.SimpleUserInfo> findChildListByParentId(long parentId){
        //parentId에 해당하는 유저가 없는 경우
        User parent = userRepository.findById(parentId).orElseThrow(
                () -> ExceptionFactory.userNotFound(parentId));

        //해당 유저가 부모 유저가 아닌 경우
        if(parent.getUserType() != UserType.PARENTS)
            throw ExceptionFactory.userTypeMismatch(parentId, UserType.PARENTS);

        List<ChildInformation> childInformations = childInformationRepository.findAllByParentId(parentId);

        //주어진 parentId와 연결된 자녀들의 userId와 프로필 이미지를 리스트에 넣어서 return
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
        //childId에 해당하는 유저가 없는 경우
        User child = userRepository.findById(childId).orElseThrow(
                () -> ExceptionFactory.userNotFound(childId));

        //해당 유저가 자녀 유저가 아닌 경우
        if(child.getUserType() != UserType.CHILD)
            throw ExceptionFactory.userTypeMismatch(childId, UserType.CHILD);

        //해당 유저와 연결된 부모가 없는 경우
        ChildInformation childInformation = childInformationRepository.findByChildId(childId).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "부모와 연결되지 않은 유저입니다. userId: " + childId));

        return ChildInformationResponseDto.ChildInfoDetail.builder()
                .childId(childId)
                .currentReward(childInformation.getCurrentReward())
                .streakCount(childInformation.getStreakCount())
                .targetBedTime(childInformation.getTargetBedTime())
                .targetWakeupTime(childInformation.getTargetWakeupTime())
                .build();
    }

    public void modifyCurrentReward(ChildInformationRequestDto.CurrentRewardInput currentRewardInput){
        long childId = currentRewardInput.getChildId();
        //childId에 해당하는 유저가 없는 경우
        User child = userRepository.findById(childId).orElseThrow(
                () -> ExceptionFactory.userNotFound(childId));

        //해당 유저가 자녀 유저가 아닌 경우
        if(child.getUserType() != UserType.CHILD)
            throw ExceptionFactory.userTypeMismatch(childId, UserType.CHILD);

        //해당 유저와 연결된 부모가 없는 경우
        ChildInformation childInformation = childInformationRepository.findByChildId(childId).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "부모와 연결되지 않은 유저입니다. userId: " + childId));

        childInformationRepository.save(
                childInformation.toBuilder()
                        .currentReward(currentRewardInput.getReward())
                        .build());
    }

    public void modifyTargetSleepTime(ChildInformationRequestDto.TargetSleepInput targetSleepInput){
        long childId = targetSleepInput.getChildId();
        //childId에 해당하는 유저가 없는 경우
        User child = userRepository.findById(childId).orElseThrow(
                () -> ExceptionFactory.userNotFound(childId));

        //해당 유저가 자녀 유저가 아닌 경우
        if(child.getUserType() != UserType.CHILD)
            throw ExceptionFactory.userTypeMismatch(childId, UserType.CHILD);

        //해당 유저와 연결된 부모가 없는 경우
        ChildInformation childInformation = childInformationRepository.findByChildId(childId).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "부모와 연결되지 않은 유저입니다. userId: " + childId));

        childInformationRepository.save(
                childInformation.toBuilder()
                        .targetBedTime(targetSleepInput.getTargetBedTime())
                        .targetWakeupTime(targetSleepInput.getTargetWakeupTime())
                        .build());
    }

    public void modifyStreakCntPlus(long childId){
        //childId에 해당하는 유저가 없는 경우
        User child = userRepository.findById(childId).orElseThrow(
                () -> ExceptionFactory.userNotFound(childId));

        //해당 유저가 자녀 유저가 아닌 경우
        if(child.getUserType() != UserType.CHILD)
            throw ExceptionFactory.userTypeMismatch(childId, UserType.CHILD);

        //해당 유저와 연결된 부모가 없는 경우
        ChildInformation childInformation = childInformationRepository.findByChildId(childId).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "부모와 연결되지 않은 유저입니다. userId: " + childId));

        childInformationRepository.save(
                childInformation.toBuilder()
                        .streakCount(childInformation.getStreakCount() + 1)
                        .build());
    }

    public void removeChildInformationByChildId(long childId){
        //childId에 해당하는 유저가 없는 경우
        User child = userRepository.findById(childId).orElseThrow(
                () -> ExceptionFactory.userNotFound(childId));

        //해당 유저가 자녀 유저가 아닌 경우
        if(child.getUserType() != UserType.CHILD)
            throw ExceptionFactory.userTypeMismatch(childId, UserType.CHILD);

        //해당 유저와 연결된 부모가 없는 경우
        ChildInformation childInformation = childInformationRepository.findByChildId(childId).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "부모와 연결되지 않은 유저입니다. userId: " + childId));

        childInformationRepository.delete(childInformation);
    }
}
