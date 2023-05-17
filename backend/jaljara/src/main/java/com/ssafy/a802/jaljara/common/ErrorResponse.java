package com.ssafy.a802.jaljara.common;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {
    private String message;
    private String timestamp;
}