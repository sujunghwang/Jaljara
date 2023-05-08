package com.ssafy.a802.jaljara.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

public class SleepLogResponseDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SleepLogDetail {
        private long userId;
        private Date date;
        private String bedTime;
        private String wakeupTime;
        private double sleepRate;
    }
}
