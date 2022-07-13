package com.devnus.belloga.auth.account.adapter.in.web;

import com.devnus.belloga.auth.account.adapter.in.web.request.RegisterEnterpriseRequest;
import com.devnus.belloga.auth.account.application.port.in.RegisterEnterpriseCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {

    @PostMapping("/v1/auth/enterprise/signup")
    void registerEnterprise(@RequestBody RegisterEnterpriseRequest registerEnterpriseRequest) {

        RegisterEnterpriseCommand command = RegisterEnterpriseCommand.builder()
                .email(registerEnterpriseRequest.getEmail())
                .name(registerEnterpriseRequest.getName())
                .organization(registerEnterpriseRequest.getOrganization())
                .password(registerEnterpriseRequest.getPassword())
                .phoneNumber(registerEnterpriseRequest.getPhoneNumber())
                .build();
    }
}
