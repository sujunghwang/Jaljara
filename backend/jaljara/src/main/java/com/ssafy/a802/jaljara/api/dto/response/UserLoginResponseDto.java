package com.ssafy.a802.jaljara.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class UserLoginResponseDto {
    private UserResponseDto.SimpleUserInfo userInfo;
    private String accessToken;
    private String refreshToken;
}
