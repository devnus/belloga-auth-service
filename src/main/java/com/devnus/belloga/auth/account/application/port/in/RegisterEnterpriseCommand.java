package com.devnus.belloga.auth.account.application.port.in;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RegisterEnterpriseCommand {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String organization;

    @Builder
    public RegisterEnterpriseCommand(String email, String password, String name, String phoneNumber, String organization) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.organization = organization;
    }
}
