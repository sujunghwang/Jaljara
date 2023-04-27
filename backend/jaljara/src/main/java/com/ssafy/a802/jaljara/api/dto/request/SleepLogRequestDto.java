package com.ssafy.a802.jaljara.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

public class SleepLogRequestDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SleepLogInput {
        private long userId;
        private Date date;
        private Time bedTime;
        private Time wakeupTime;
    }
}
