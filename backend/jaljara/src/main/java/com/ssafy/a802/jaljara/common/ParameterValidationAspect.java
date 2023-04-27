package com.ssafy.a802.jaljara.common;

import com.ssafy.a802.jaljara.api.dto.request.ChildInformationRequestDto;
import com.ssafy.a802.jaljara.api.dto.request.SleepLogRequestDto;
import com.ssafy.a802.jaljara.db.entity.User;
import com.ssafy.a802.jaljara.db.entity.UserType;
import com.ssafy.a802.jaljara.db.repository.UserRepository;
import com.ssafy.a802.jaljara.exception.ExceptionFactory;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ParameterValidationAspect {
    private final UserRepository userRepository;

    @Autowired
    public ParameterValidationAspect(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Before("@annotation(com.ssafy.a802.jaljara.common.annotation.ValidChildIdParameter) && args(childId, ..)")
    public void validationChild(long childId) {
        //childId에 해당하는 유저가 없는 경우
        User child = userRepository.findById(childId).orElseThrow(
                () -> ExceptionFactory.userNotFound(childId));

        //해당 유저가 자녀 유저가 아닌 경우
        if(child.getUserType() != UserType.CHILD)
            throw ExceptionFactory.userTypeMismatch(childId, UserType.CHILD);
    }

    @Before("@annotation(com.ssafy.a802.jaljara.common.annotation.ValidParentIdParameter) && args(parentId, ..)")
    public void validationParent(long parentId) {
        //parentId에 해당하는 유저가 없는 경우
        User parent = userRepository.findById(parentId).orElseThrow(
                () -> ExceptionFactory.userNotFound(parentId));

        //해당 유저가 부모 유저가 아닌 경우
        if(parent.getUserType() != UserType.PARENTS)
            throw ExceptionFactory.userTypeMismatch(parentId, UserType.PARENTS);
    }

    @Before("@annotation(com.ssafy.a802.jaljara.common.annotation.ValidChildIdParameter) && args(currentRewardInput, ..)")
    public void validationChild(ChildInformationRequestDto.CurrentRewardInput currentRewardInput) {
        validationChild(currentRewardInput.getChildId());
    }

    @Before("@annotation(com.ssafy.a802.jaljara.common.annotation.ValidChildIdParameter) && args(targetSleepInput, ..)")
    public void validationChild(ChildInformationRequestDto.TargetSleepInput targetSleepInput) {
        validationChild(targetSleepInput.getChildId());
    }

    @Before("@annotation(com.ssafy.a802.jaljara.common.annotation.ValidChildIdParameter) && args(sleepLogInput, ..)")
    public void validationChild(SleepLogRequestDto.SleepLogInput sleepLogInput) {
        validationChild(sleepLogInput.getUserId());
    }

    @Before("@annotation(com.ssafy.a802.jaljara.common.annotation.ValidParentAndChildIdParameter) && args(parentId, childId, ..)")
    public void validationParentAndChild(long parentId, long childId) {
        validationParent(parentId);
        validationChild(childId);
    }
}
