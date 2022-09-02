package com.devnus.belloga.auth.account.service;

import com.devnus.belloga.auth.account.dto.ResponseOauth;
import com.devnus.belloga.auth.common.exception.error.InvalidOauthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OauthWebClient {
    private final WebClient naverWebClient;

    /**
     * 동기 통신을 통해 네이버 API에서 로그인 후 발급받은 토큰을 이용해 사용자 정보를 가져온다
     */
    public ResponseOauth.UserInfo getNaverInfo(String token) {
        JSONObject jsonObject = naverWebClient
                .get()
                .uri("/v1/nid/me")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .flatMap(res -> {
                    if(res.statusCode().value() == HttpStatus.UNAUTHORIZED.value()){
                        throw new InvalidOauthException();
                    }
                    return res.bodyToMono(JSONObject.class);
                })
                .block();

        Map<String, Object> response = (Map<String, Object>) jsonObject.get("response");

        return ResponseOauth.UserInfo.builder()
                .birthYear((String) response.get("birthyear"))
                .email((String) response.get("email"))
                .id((String) response.get("id"))
                .phoneNumber((String) response.get("mobile"))
                .name((String) response.get("nickname"))
                .build();
    }
}
