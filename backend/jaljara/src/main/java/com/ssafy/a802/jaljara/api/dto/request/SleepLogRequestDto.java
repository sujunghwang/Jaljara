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
    public static class SleepSegmentEvent {
        public static final int STATUS_SUCCESSFUL = 0;
        public static final int STATUS_MISSING_DATA = 1;
        public static final int STATUS_NOT_DETECTED = 2;

        private Long userId;
        private Long startTimeMillis;
        private Long endTimeMills;
        private Long segmentDurationMillis;
        private Integer status;
    }
}
