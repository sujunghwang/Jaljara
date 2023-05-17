package com.ssafy.a802.jaljara.api.dto.response;

import lombok.*;

@Builder
@Data
public class TokenRefreshResponseDto {
    private String accessToken;
    private String refreshToken;
}
