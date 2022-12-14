package com.devnus.belloga.auth.common.util;

import com.devnus.belloga.auth.account.dto.ResponseAuth;
import com.devnus.belloga.auth.common.exception.error.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static String secretKey; //설정파일의 토큰 Key
    private static Long accessTokenValidTime; //access 토큰 유효시간
    private static Long refreshTokenValidTime; //refresh 토큰 유효시간

    /**
     * static 변수에 @value로 값을 주입하기 위해 setter 사용
     */
    @Value("${app.jwt.secret-key}")
    public void setSecretKey(String secretKey) {
        JwtUtil.secretKey = secretKey;
    }
    @Value("${app.jwt.access-token-valid-time}")
    public void setAccessTokenValidTime(Long accessTokenValidTime) {
        JwtUtil.accessTokenValidTime = accessTokenValidTime;
    }
    @Value("${app.jwt.refresh-token-valid-time}")
    public void setRefreshTokenValidTime(Long refreshTokenValidTime) {
        JwtUtil.refreshTokenValidTime = refreshTokenValidTime;
    }

    public static ResponseAuth.Token generate(String encryptedUserId, String userRole) {

        //Header 설정
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

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

        return ResponseAuth.Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static boolean validate(String token){
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) { //서명이 잘못 되었을때
            throw new InvalidTokenException();
        } catch (ExpiredJwtException e) { //기간이 만료되었을때
            throw new InvalidTokenException();
        } catch (UnsupportedJwtException e) { //지원하지 않는 JWT 토큰일때
            throw new InvalidTokenException();
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
    }

    public static String getEncryptedUserId(String token){
        String payload = token.split("\\.")[1];
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String decodedPayload = new String(decoder.decode(payload));
        JsonParser jsonParser = new BasicJsonParser();
        Map<String,Object> jsonArray = jsonParser.parseMap(decodedPayload);

        if(!jsonArray.containsKey("userId")){
            throw new InvalidTokenException();
        }

        return jsonArray.get("userId").toString();
    }

    public static String getUserRole(String token){
        String payload = token.split("\\.")[1];
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String decodedPayload = new String(decoder.decode(payload));
        JsonParser jsonParser = new BasicJsonParser();
        Map<String,Object> jsonArray = jsonParser.parseMap(decodedPayload);

        if(!jsonArray.containsKey("userRole")){
            throw new InvalidTokenException();
        }

        return jsonArray.get("userRole").toString();
    }
}
