package com.devnus.belloga.auth.account.controller;

import com.devnus.belloga.auth.account.dto.RequestAccount;
import com.devnus.belloga.auth.account.dto.ResponseUser;
import com.devnus.belloga.auth.account.service.AccountService;
import com.devnus.belloga.auth.account.service.AuthService;
import com.devnus.belloga.auth.account.service.UserWebClient;
import com.devnus.belloga.auth.common.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * AccountController는 계정 관련 컨트롤러
 * @author 조광식
 */
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final UserWebClient userWebClient;
    private final AuthService authService;

    /**
     * 기업 사용자의 자체 계정 회원가입
     */
    @PostMapping("/v1/auth/signup/custom/account/enterprise")
    public ResponseEntity<CommonResponse> registerCustomAccountEnterprise(@Valid @RequestBody RequestAccount.RegisterCustomAccountEnterprise request) {

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(accountService.saveCustomAccountEnterprise(request))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 기업 사용자의 자체 계정 로그인
     */
    @PostMapping("/v1/auth/signin/custom/account")
    public ResponseEntity<CommonResponse> signInCustomAccount(@Valid @RequestBody RequestAccount.SignInCustomAccount request) {

        String accountId = accountService.authenticateCustomAccount(request); //계정 인증
        ResponseUser.UserInfo userInfo = userWebClient.getUserInfo(accountId); //계정 ID를 통해 사용자 정보 가져옴

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(authService.generateToken(userInfo.getUserId(), userInfo.getUserRole()))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
