package com.devnus.belloga.auth.account.service;

import com.devnus.belloga.auth.account.dto.ResponseAuth;
import com.devnus.belloga.auth.common.aop.annotation.UserRole;
import com.devnus.belloga.auth.common.exception.error.EncryptException;
import com.devnus.belloga.auth.common.util.SecurityUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService{
    private final String secretKey; //설정파일의 토큰 Key
    private final Long accessTokenValidTime; //access 토큰 유효시간
    private final Long refreshTokenValidTime; //refresh 토큰 유효시간
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 생성자 주입
     */
    public AuthServiceImpl(@Value("${app.jwt.secret-key}")String SECRET_KEY,
                           @Value("${app.jwt.access-token-valid-time}")Long ACCESS_TOKEN_VALID_TIME,
                           @Value("${app.jwt.refresh-token-valid-time}")Long REFRESH_TOKEN_VALID_TIME,
                           RedisTemplate<String, String> redisTemplate) {
        this.secretKey = SECRET_KEY;
        this.accessTokenValidTime = ACCESS_TOKEN_VALID_TIME;
        this.refreshTokenValidTime = REFRESH_TOKEN_VALID_TIME;
        this.redisTemplate = redisTemplate;
    }


    /**
     * 엑세스 토큰과 리프레쉬 토큰생성
     */
    @Override
    public ResponseAuth.Token generateToken(String accountId, UserRole userRole) {

        //Header 설정
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        //accountId 암호화
        String encryptedUserId;
        try{
            encryptedUserId = SecurityUtil.encryptAES256(accountId + new Date(System.currentTimeMillis()));
        } catch (Exception e) {
            throw new EncryptException(e);
        }

        //payload 설정
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("userId", encryptedUserId);
        payloads.put("userRole", userRole);

        Date accessTokenExpirationDate = new Date(System.currentTimeMillis() + accessTokenValidTime * 1000L); //엑세스 토큰 만료 시간
        Date refreshTokenExpirationDate = new Date(System.currentTimeMillis() + refreshTokenValidTime * 1000L); //리프레시 토큰 만료 시간

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        //accessToken 생성
        String accessToken = Jwts.builder()
                .setHeader(headers) //Headers 설정
                .setClaims(payloads) //Claims 설정
                .setSubject("accessToken") //토큰 용도
                .setExpiration(accessTokenExpirationDate) //토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        //refreshToken 생성
        String refreshToken = Jwts.builder()
                .setHeader(headers) //Headers 설정
                .setClaims(payloads) //Claims 설정
                .setSubject("refreshToken") //토큰 용도
                .setExpiration(refreshTokenExpirationDate) //토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        //refreshToken 정보 Redis에 만료시간을 설정해서 저장 (key:encryptedUserId, value:refreshToken)
        //키에 이미 값이 있으면 덮어 쓴다
        redisTemplate.opsForValue().set(encryptedUserId, refreshToken, Duration.ofSeconds(refreshTokenValidTime));

        return ResponseAuth.Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
