package com.devnus.belloga.auth.account.service;

import com.devnus.belloga.auth.account.dto.ResponseAuth;
import com.devnus.belloga.auth.common.aop.annotation.UserRole;
import com.devnus.belloga.auth.common.exception.error.EncryptException;
import com.devnus.belloga.auth.common.exception.error.InvalidTokenException;
import com.devnus.belloga.auth.common.util.JwtUtil;
import com.devnus.belloga.auth.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService{
    private final Long refreshTokenValidTime; //refresh 토큰 유효시간
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 생성자 주입
     */
    public AuthServiceImpl(@Value("${app.jwt.refresh-token-valid-time}")Long REFRESH_TOKEN_VALID_TIME, RedisTemplate<String, String> redisTemplate) {
        this.refreshTokenValidTime = REFRESH_TOKEN_VALID_TIME;
        this.redisTemplate = redisTemplate;
    }


    /**
     * 엑세스 토큰과 리프레쉬 토큰생성
     */
    @Override
    public ResponseAuth.Token generateToken(String accountId, UserRole userRole) {

        //accountId 암호화
        String encryptedUserId;
        try{
            encryptedUserId = SecurityUtil.encryptAES256(accountId + new Date(System.currentTimeMillis()));
        } catch (Exception e) {
            throw new EncryptException(e);
        }

        //토큰 생성
        ResponseAuth.Token token = JwtUtil.generate(encryptedUserId, userRole.name());

        //refreshToken 정보 Redis에 만료시간을 설정해서 저장 (key:encryptedUserId, value:refreshToken)
        //키에 이미 값이 있으면 덮어 쓴다
        redisTemplate.opsForValue().set(encryptedUserId, token.getRefreshToken(), Duration.ofSeconds(refreshTokenValidTime));

        return token;
    }

    /**
     * 리프레쉬 토큰을 이용한 엑세스 토큰 재 발급
     */
    @Override
    public ResponseAuth.Token reissueToken(String refreshToken){

        //refreshToken 검증
        JwtUtil.validate(refreshToken);

        String encryptedUserIdByToken = JwtUtil.getEncryptedUserId(refreshToken);
        String userRoleByToken = JwtUtil.getUserRole(refreshToken);

        String refreshTokenByRedis = redisTemplate.opsForValue().get(encryptedUserIdByToken);

        // Redis 의 refreshToken 값과 비교
        if(!refreshToken.equals(refreshTokenByRedis)){
            throw new InvalidTokenException();
        }

        //토큰 재 발급

        //토큰 생성
        ResponseAuth.Token token = JwtUtil.generate(encryptedUserIdByToken, userRoleByToken);

        //refreshToken 정보 Redis에 만료시간을 설정해서 저장 (key:encryptedUserId, value:refreshToken)
        //키에 이미 값이 있으면 덮어 쓴다
        redisTemplate.opsForValue().set(encryptedUserIdByToken, token.getRefreshToken(), Duration.ofSeconds(refreshTokenValidTime));

        return token;
    }
}
