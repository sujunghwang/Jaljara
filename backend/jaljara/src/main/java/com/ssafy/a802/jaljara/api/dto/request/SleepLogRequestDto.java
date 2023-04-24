package com.ssafy.a802.jaljara.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SleepLogRequestDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SleepLogInput {
        private long userId;
        private LocalDate date;
        private LocalDateTime bedTime;
        private LocalDateTime wakeupTime;
    }
}
