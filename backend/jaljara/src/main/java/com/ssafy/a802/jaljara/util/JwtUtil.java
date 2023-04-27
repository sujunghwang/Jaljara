package com.ssafy.a802.jaljara.util;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ssafy.a802.jaljara.db.entity.UserType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    private JwkProvider kakaoJwkProvider;
    private Algorithm algorithm;
    private static String SECRET_KEY;
    private static String ISSUER;
    private static long ACCESS_EXPIRATION_TIME;
    private static long REFRESH_EXPIRATION_TIME;


    public JwtUtil(@Value("${jwt.secret}") String secretKey,
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

    public Jwk getKakaoJwk(DecodedJWT decodedJwt) {
        Jwk jwk = null;
        try {
            jwk = kakaoJwkProvider.get(decodedJwt.getKeyId());
        } catch (JwkException e) {
        }

        return jwk;
    }

    public String createAccessToken(Long id, String name, UserType userType) {
        return createToken(id, name, userType, false);
    }

    public String createRefreshToken(Long id, String name, UserType userType) {
        return createToken(id, name, userType, true);
    }

    public Long verifyJwtTokenAndClaimId(String token) {
        return JWT.require(algorithm).build().verify(token).getClaim("id").asLong();
    }

    private String createToken(Long id, String name, UserType userType, boolean isRefresh) {
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

    private Date calculateExpirationTime(Date now, boolean isRefresh) {
        return isRefresh ?
                new Date(now.getTime() + ACCESS_EXPIRATION_TIME)
                : new Date(now.getTime() + REFRESH_EXPIRATION_TIME);
    }

}
