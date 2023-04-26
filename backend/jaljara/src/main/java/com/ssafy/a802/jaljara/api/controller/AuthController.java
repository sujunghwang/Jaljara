package com.ssafy.a802.jaljara.api.controller;

import com.ssafy.a802.jaljara.api.dto.request.TokenRefreshRequestDto;
import com.ssafy.a802.jaljara.api.dto.request.UserLoginRequestDto;
import com.ssafy.a802.jaljara.api.dto.response.UserLoginResponseDto;
import com.ssafy.a802.jaljara.api.dto.response.UserResponseDto.SimpleUserInfo;
import com.ssafy.a802.jaljara.api.service.AuthService;
import com.ssafy.a802.jaljara.db.entity.User;
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
        Optional<User> user = authService.signupWithAnyProvider(userLoginRequestDto);

        return ResponseEntity.ok(new SimpleUserInfo().builder()
                .userId(user.get().getId())
                .profileImageUrl(user.get().getProfileImageUrl())
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequestDto tokenRefreshRequestDto) {
        throw new NotYetImplementedException("Todo: add refresh controller");
    }
}
