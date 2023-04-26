package com.ssafy.a802.jaljara.exception;

import com.ssafy.a802.jaljara.db.entity.UserType;
import org.springframework.http.HttpStatus;

public final class ExceptionFactory {
    private ExceptionFactory() {}

    public static CustomException userNotFound(long userId){
        return new CustomException(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다. userId: " + userId);
    }

    public static CustomException userTypeMismatch(long userId, UserType type){
        if(type == UserType.PARENTS)
            return new CustomException(HttpStatus.BAD_REQUEST, "해당 유저는 부모 유저가 아닙니다. userId: " + userId);
        else
            return new CustomException(HttpStatus.BAD_REQUEST, "해당 유저는 자녀 유저가 아닙니다. userId: " + userId);
    }

    public static CustomException userMissionTodayNotFound(long userId) {
        return new CustomException(HttpStatus.NOT_FOUND, "해당 유저에게 오늘의 미션이 존재하지 않습니다. userIdl " + userId);
    }
}
