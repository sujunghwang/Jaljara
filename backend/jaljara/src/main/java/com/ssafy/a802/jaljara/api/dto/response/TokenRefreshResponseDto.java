package com.ssafy.a802.jaljara.api.dto.response;

import lombok.*;

@Data
public class TokenRefreshResponseDto {
    private String accessToken;
    private String refreshToken;
}
