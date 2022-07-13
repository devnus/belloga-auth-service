package com.devnus.belloga.auth.account.adapter.in.web.request;

import lombok.Getter;

@Getter
public class RegisterEnterpriseRequest {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String organization;
}
