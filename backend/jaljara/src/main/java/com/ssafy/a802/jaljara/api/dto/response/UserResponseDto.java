package com.ssafy.a802.jaljara.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponseDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SimpleUserInfo {
        private long userId;
        private String name;
        private String profileImageUrl;
        private String userType;
    }
}
