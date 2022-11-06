package com.devnus.belloga.auth.account.controller;

import com.devnus.belloga.auth.account.domain.AuthProvider;
import com.devnus.belloga.auth.account.dto.RequestAccount;
import com.devnus.belloga.auth.account.dto.RequestAuth;
import com.devnus.belloga.auth.account.dto.ResponseAccount;
import com.devnus.belloga.auth.account.dto.ResponseOauth;
import com.devnus.belloga.auth.account.service.AccountService;
import com.devnus.belloga.auth.account.service.AuthService;
import com.devnus.belloga.auth.account.service.OauthWebClient;
import com.devnus.belloga.auth.common.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 계정 관련 컨트롤러
 */
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final AccountService accountService;
    private final OauthWebClient oauthWebClient;
    private final AuthService authService;

    /**
     * 기업 사용자의 자체 계정 회원가입
     */
    @PostMapping("/v1/auth/signup/custom/account/enterprise")
    public ResponseEntity<CommonResponse> registerCustomAccountEnterprise(@Valid @RequestBody RequestAccount.RegisterCustomAccountEnterprise request) {
        logger.info("register custom account enterprise auth-service");
        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(accountService.saveCustomAccountEnterprise(request))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 관리자의 자체 계정 회원가입
     */
    @PostMapping("/v1/auth/signup/custom/account/admin")
    public ResponseEntity<CommonResponse> registerCustomAccountAdmin(@Valid @RequestBody RequestAccount.RegisterCustomAccountAdmin request) {
        logger.info("register custom account enterprise auth-service");
        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(accountService.saveCustomAccountAdmin(request))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Custom 사용자의 자체 계정 로그인
     */
    @PostMapping("/v1/auth/signin/custom/account")
    public ResponseEntity<CommonResponse> signInCustomAccount(@Valid @RequestBody RequestAccount.SignInCustomAccount request) {
        logger.info("login custom account enterprise auth-service");
        ResponseAccount.SignInAccount signInAccount = accountService.authenticateCustomAccount(request);

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(authService.generateToken(signInAccount.getAccountId(), signInAccount.getUserRole()))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 일반 사용자의 네이버 계정 로그인
     * 앱에서 로그인 후 받은 토큰으로 로그인
     */
    @PostMapping("/v1/auth/signin/naver/account")
    public ResponseEntity<CommonResponse> signInNaverAccount(@Valid @RequestBody RequestAccount.SignInNaverAccount request) {
        logger.info("register sign in naver account auth-service");
        ResponseOauth.UserInfo oauthInfo = oauthWebClient.getNaverInfo(request.getToken()); //토큰을 통해 네이버 사용자 정보를 가져옴
        ResponseAccount.SignInAccount signInAccount = accountService.authenticateOauthAccount(oauthInfo, AuthProvider.NAVER); //계정 인증

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(authService.generateToken(signInAccount.getAccountId(), signInAccount.getUserRole()))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 리프레쉬 토큰을 이용해 엑세스 토큰 재 발급
     */
    @PostMapping("/v1/auth/reissue")
    public ResponseEntity<CommonResponse> reissue(@Valid @RequestBody RequestAuth.Reissue request) {
        logger.info("reissue token in auth-service");

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(authService.reissueToken(request.getRefreshToken()))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
