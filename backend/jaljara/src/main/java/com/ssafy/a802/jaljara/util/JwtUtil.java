package com.ssafy.a802.jaljara.util;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ssafy.a802.jaljara.api.dto.response.UserResponseDto;
import com.ssafy.a802.jaljara.db.entity.UserType;
import com.ssafy.a802.jaljara.exception.ExceptionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    private static JwkProvider kakaoJwkProvider;
    private static Algorithm algorithm;
    private static String SECRET_KEY;
    private static String ISSUER;
    private static long ACCESS_EXPIRATION_TIME;
    private static long REFRESH_EXPIRATION_TIME;


    private JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.issuer}") String issuer,
                   @Value("${jwt.access_exp_time}") long accessExpTime,
                   @Value("${jwt.refresh_exp_time}") long refreshExpTime) {
        kakaoJwkProvider = new JwkProviderBuilder("https://kauth.kakao.com")
                .cached(10, 1, TimeUnit.HOURS)
                .build();

        SECRET_KEY = secretKey;
        ACCESS_EXPIRATION_TIME = accessExpTime;
        REFRESH_EXPIRATION_TIME = refreshExpTime;
        ISSUER = issuer;
        algorithm = Algorithm.HMAC256(SECRET_KEY);
    }

    public static Jwk getKakaoJwk(DecodedJWT decodedJwt) {
        Jwk jwk = null;
        try {
            jwk = kakaoJwkProvider.get(decodedJwt.getKeyId());
        } catch (JwkException e) {
        }

        return jwk;
    }

    public static String createAccessToken(Long id, String name, UserType userType) {
        return createToken(id, name, userType, false);
    }

    public static String createRefreshToken(Long id, String name, UserType userType) {
        return createToken(id, name, userType, true);
    }

    public static Boolean isValidToken(String token) {
        return verifyJwtToken(token);
    }

    public static Long claimIdFromToken(String token) {
        return verifyJwtTokenAndClaimId(token);
    }

    public static Authentication getAuthentication(String token) {

        Long id = verifyJwtTokenAndClaimId(token);

        if (id == null) {
            throw ExceptionFactory.jwtAuthenticateFail();
        }

        return new UsernamePasswordAuthenticationToken(UserResponseDto.SimpleUserInfo.builder().userId(id).build(), "", new ArrayList<>());
    }

    private static Boolean verifyJwtToken(String token) {
        JWT.require(algorithm).build().verify(token);
        return true;
    }

    private static Long verifyJwtTokenAndClaimId(String token) {
        return verifyJwtTokenAndGetClaims(token).get("id").asLong();
    }

    private static Map<String, Claim> verifyJwtTokenAndGetClaims(String token) {
        return JWT.require(algorithm).build().verify(token).getClaims();
    }

    private static String createToken(Long id, String name, UserType userType, boolean isRefresh) {
        JWTCreator.Builder builder = JWT.create();
        Date now = new Date();

        return builder.withIssuer(ISSUER)
                .withClaim("id", id)
                .withClaim("name", name)
                .withClaim("userType", userType.name())
                .withIssuedAt(now)
                .withExpiresAt(calculateExpirationTime(now, isRefresh))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    private static Date calculateExpirationTime(Date now, boolean isRefresh) {
        return isRefresh ?
                new Date(now.getTime() + ACCESS_EXPIRATION_TIME)
                : new Date(now.getTime() + REFRESH_EXPIRATION_TIME);
    }

}
