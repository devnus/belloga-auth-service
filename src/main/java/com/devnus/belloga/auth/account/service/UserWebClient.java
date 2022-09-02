package com.devnus.belloga.auth.account.service;

import com.devnus.belloga.auth.account.dto.RequestUser;
import com.devnus.belloga.auth.account.dto.ResponseUser;
import com.devnus.belloga.auth.common.dto.CommonResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserWebClient {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    /**
     * 동기 통신을 통해 User마이크로서비스에서 accountId를 이용해 해당하는 UserId와 UserRole을 가져온다
     */
    public ResponseUser.UserInfo getUserInfo(String accountId) {

        CommonResponse commonResponse = webClient
                .get()
                .uri("/api/user/v1/users/accounts/{accountId}", accountId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CommonResponse.class)
                .block();

        //LinkedHashMap으로 역직렬화 된 commonResponse의 response 값을 ResponseUser.UserInfo타입으로 형변환
        return objectMapper.convertValue(commonResponse.getResponse(), new TypeReference<ResponseUser.UserInfo>() {});
    }

    public ResponseUser.UserInfo saveUserInfo(RequestUser.RegisterOauthUser request) {

        CommonResponse commonResponse = webClient
                .post()
                .uri("/api/user/v1/user")
                .bodyValue(request)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CommonResponse.class)
                .block();

        //LinkedHashMap으로 역직렬화 된 commonResponse의 response 값을 ResponseUser.UserInfo타입으로 형변환
        return objectMapper.convertValue(commonResponse.getResponse(), new TypeReference<ResponseUser.UserInfo>() {});
    }
}
