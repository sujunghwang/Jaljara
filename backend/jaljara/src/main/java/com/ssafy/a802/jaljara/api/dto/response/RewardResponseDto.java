package com.ssafy.a802.jaljara.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class RewardResponseDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RewardUsable {
        private long rewardId;
        private long userId;
        private String content;
        private LocalDateTime getTime;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RewardUsed {
        private long rewardId;
        private long userId;
        private String content;
        private LocalDateTime getTime;
        private LocalDateTime usedTime;
    }
}
