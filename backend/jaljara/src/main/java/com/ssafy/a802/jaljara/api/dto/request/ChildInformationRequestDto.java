package com.ssafy.a802.jaljara.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

public class ChildInformationRequestDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CurrentRewardInput {
        private long childId;
        private String reward;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TargetSleepInput {
        private long childId;
        private Time targetBedTime;
        private Time targetWakeupTime;
    }
}
