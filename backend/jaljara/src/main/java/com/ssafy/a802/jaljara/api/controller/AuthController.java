package com.ssafy.a802.jaljara.api.controller;

import com.ssafy.a802.jaljara.api.dto.request.TokenRefreshRequestDto;
import com.ssafy.a802.jaljara.api.dto.request.UserLoginRequestDto;
import com.ssafy.a802.jaljara.api.dto.response.UserLoginResponseDto;
import com.ssafy.a802.jaljara.api.dto.response.UserResponseDto.SimpleUserInfo;
import com.ssafy.a802.jaljara.api.service.AuthService;
import com.ssafy.a802.jaljara.db.entity.User;
import com.ssafy.a802.jaljara.exception.ExceptionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        log.info(userLoginRequestDto.getProvider().name());
        UserLoginResponseDto userLoginResponseDto = authService.loginWithAnyProvider(userLoginRequestDto);

        return ResponseEntity.ok(userLoginResponseDto);
    }

    @PostMapping("/parent/signup")
    public ResponseEntity<?> parentSignUp(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        User user = authService.signupWithAnyProvider(userLoginRequestDto).orElseThrow();

        return ResponseEntity.ok(new SimpleUserInfo().builder()
                .userId(user.getId())
                .profileImageUrl(user.getProfileImageUrl())
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequestDto tokenRefreshRequestDto) {
        return ResponseEntity.ok(authService.refreshTokens(tokenRefreshRequestDto));
    }
}
