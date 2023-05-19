package com.ssafy.a802.jaljara.api.controller;

import com.ssafy.a802.jaljara.api.dto.request.TokenRefreshRequestDto;
import com.ssafy.a802.jaljara.api.dto.request.UserLoginRequestDto;
import com.ssafy.a802.jaljara.api.dto.response.UserLoginResponseDto;
import com.ssafy.a802.jaljara.api.dto.response.UserResponseDto.SimpleUserInfo;
import com.ssafy.a802.jaljara.api.service.AuthService;
import com.ssafy.a802.jaljara.api.service.ChildInformationService;
import com.ssafy.a802.jaljara.api.service.ParentCodeService;
import com.ssafy.a802.jaljara.db.entity.ParentCode;
import com.ssafy.a802.jaljara.db.entity.User;
import com.ssafy.a802.jaljara.exception.ExceptionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ParentCodeService parentCodeService;
    private final ChildInformationService childInformationService;

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        UserLoginResponseDto userLoginResponseDto = authService.loginWithAnyProvider(userLoginRequestDto);

        return ResponseEntity.ok(userLoginResponseDto);
    }

    @PostMapping("/parent/signup")
    public ResponseEntity<?> parentSignUp(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        User user = authService.signupWithAnyProvider(userLoginRequestDto);

        return ResponseEntity.ok(new SimpleUserInfo().builder()
                .userId(user.getId())
                .profileImageUrl(user.getProfileImageUrl())
                .userType(user.getUserType().name())
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequestDto tokenRefreshRequestDto) {
        return ResponseEntity.ok(authService.refreshTokens(tokenRefreshRequestDto));
    }

    @GetMapping("/parent/code")
    public ResponseEntity<?> getNewParentCode(@RequestParam long parentId) {
        return ResponseEntity.ok(parentCodeService.createParentCode(parentId));
    }

    @PostMapping("/child/signup/{code}")
    public ResponseEntity<?> childSignUp(@RequestBody UserLoginRequestDto userLoginRequestDto, @PathVariable String code) {
        ParentCode parentCode = parentCodeService.getParentCodeEntity(code);
        User user = authService.signupWithAnyProviderAsChild(userLoginRequestDto, code);

        childInformationService.addChildInformation(Long.parseLong(parentCode.getParentId()), user.getId());

        return ResponseEntity.ok(new SimpleUserInfo().builder()
                .userId(user.getId())
                .profileImageUrl(user.getProfileImageUrl())
                .userType(user.getUserType().name())
                .build());
    }
}