package com.devnus.belloga.auth.account.controller;

import com.devnus.belloga.auth.account.dto.RequestAccount;
import com.devnus.belloga.auth.account.service.AccountService;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    /**
     * 기업 사용자의 자체 계정 회원가입
     * @param request
     * @return
     */
    @PostMapping("/v1/auth/signup/custom/account/enterprise")
    public ResponseEntity<CommonResponse> registerCustomAccountEnterprise(@Valid @RequestBody RequestAccount.RegisterCustomAccountEnterprise request) {

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(accountService.saveCustomAccountEnterprise(request))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
