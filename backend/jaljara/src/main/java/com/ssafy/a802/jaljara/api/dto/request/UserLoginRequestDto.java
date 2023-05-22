package com.ssafy.a802.jaljara.api.dto.request;

import com.ssafy.a802.jaljara.api.service.AuthService;
import lombok.*;

@Data
public class UserLoginRequestDto {
    private AuthService.Provider provider;
    private String token;
}
