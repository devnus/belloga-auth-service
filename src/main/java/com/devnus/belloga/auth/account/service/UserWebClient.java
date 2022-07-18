package com.devnus.belloga.auth.account.service;

import com.devnus.belloga.auth.account.dto.ResponseUser;
import com.devnus.belloga.auth.common.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserWebClient {
    private final WebClient webClient;

    /**
     * 동기 통신을 통해 User마이크로서비스에서 accountId를 이용해 해당하는 UserId와 UserRole을 가져온다
     */
    public ResponseUser.UserInfo getUserInfo(String accountId) {

        CommonResponse commonResponse = webClient
                .get()
                .uri("/api/v1/users/accounts/{accountId}", accountId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CommonResponse.class)
                .block();

        return (ResponseUser.UserInfo) commonResponse.getResponse();
    }
}
