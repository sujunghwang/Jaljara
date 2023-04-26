package com.ssafy.a802.jaljara.api.service;

import com.auth0.jwk.InvalidPublicKeyException;
import com.auth0.jwk.Jwk;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.ssafy.a802.jaljara.api.dto.request.UserLoginRequestDto;
import com.ssafy.a802.jaljara.api.dto.response.UserLoginResponseDto;
import com.ssafy.a802.jaljara.api.dto.response.UserResponseDto;
import com.ssafy.a802.jaljara.db.entity.User;
import com.ssafy.a802.jaljara.db.entity.UserType;
import com.ssafy.a802.jaljara.db.repository.UserRepository;
import com.ssafy.a802.jaljara.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    public enum Provider {
        GOOGLE,
        KAKAO
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Payload {
        private String name;
        private String sub;
        private String email;
        private String profilePictureUrl;
    }

    private final UserRepository userRepository;
    private final NetHttpTransport netHttpTransport = new NetHttpTransport();
    private final JwtUtil jwtUtil;


    @Value("${auth.google_api_id}")
    private String GOOGLE_API_ID;

    private Payload tokenVerifier(Provider provider, String token) {
        switch(provider) {
            case GOOGLE: {
                GoogleIdToken.Payload payload = googleTokenVerifier(token);

                return Payload.builder()
                        .name((String)payload.get("name"))
                        .email(payload.getEmail())
                        .sub(payload.getSubject())
                        .profilePictureUrl((String)payload.get("picture"))
                        .build();
            }
            case KAKAO:
                return kakaoTokenVerifier(token);
        }

        return new Payload();
    }

    private GoogleIdToken.Payload googleTokenVerifier(String token) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(GOOGLE_API_ID))
                .build();

        GoogleIdToken idToken = null;

        try {
            idToken = verifier.verify(token);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (idToken != null) {
            return idToken.getPayload();
        }

        return null;
    }

    private Payload kakaoTokenVerifier(String token) {
        Jwk jwk = jwtUtil.getKakaoJwk(JWT.decode(token));

        Payload payload = null;

        try {
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey());
            DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);

            payload = Payload.builder()
                    .name(decodedJWT.getClaim("nickname").asString())
                    .sub(decodedJWT.getSubject())
                    .email(decodedJWT.getClaim("email").asString())
                    .profilePictureUrl(decodedJWT.getClaim("picture").asString())
                    .build();
        } catch (InvalidPublicKeyException e) {
            throw new RuntimeException(e);
        }

        return payload;
    }

    public UserLoginResponseDto loginWithAnyProvider(UserLoginRequestDto userLoginRequestDto) {
        Payload payload = tokenVerifier(userLoginRequestDto.getProvider(), userLoginRequestDto.getToken());

        User user = userRepository.findBySub(payload.getSub()).orElseThrow();

        return UserLoginResponseDto.builder()
                .userInfo(UserResponseDto.SimpleUserInfo.builder()
                        .userId(user.getId())
                        .profileImageUrl(user.getProfileImageUrl())
                        .build())
                .accessToken(jwtUtil.createAccessToken(user.getId(), user.getName(), user.getUserType()))
                .refreshToken(jwtUtil.createRefreshToken(user.getId(), user.getName(), user.getUserType()))
                .build();
    }

    public Optional<User> signupWithAnyProvider(UserLoginRequestDto userLoginRequestDto) {
        Payload payload = tokenVerifier(userLoginRequestDto.getProvider(), userLoginRequestDto.getToken());

        if (payload != null) {
            String sub = payload.getSub();
            if(!userRepository.findBySub(sub).isPresent())
            {
                userRepository.save(User.builder()
                        .name(payload.getName())
                        .email(payload.getEmail())
                        .sub(payload.getSub())
                        .profileImageUrl(payload.getProfilePictureUrl())
                        .provider(userLoginRequestDto.getProvider().name())
                        .userType(UserType.PARENTS)
                        .build());

                return userRepository.findBySub(sub);
            }
        }

        return null;
    }
}
