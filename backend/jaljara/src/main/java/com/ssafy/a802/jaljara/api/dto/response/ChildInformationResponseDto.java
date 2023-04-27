package com.ssafy.a802.jaljara.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

public class ChildInformationResponseDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ChildInfoDetail {
        private long childId;
        private String currentReward;
        private int streakCount;
        private Time targetBedTime;
        private Time targetWakeupTime;
    }
}
