package com.ssafy.a802.jaljara.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SleepLogResponseDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SleepLogDetail {
        private long userId;
        private LocalDate date;
        private LocalDateTime bedTime;
        private LocalDateTime wakeupTime;
        private double sleepRate;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SleepLogSimple {
        private boolean isRecorded;
    }
}
