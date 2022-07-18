package com.devnus.belloga.auth.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RequestAccount {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterCustomAccountEnterprise {
        @NotNull
        @Email
        private String email;
        @NotNull
        @Size(min = 5, max = 30)
        private String password;
        @NotNull
        @Size(min = 1, max = 100)
        private String name;
        @NotNull
        @Size(min = 1, max = 100)
        private String phoneNumber;
        @NotNull
        @Size(min = 1, max = 100)
        private String organization;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignInCustomAccount {
        @NotNull
        @Email
        private String email;
        @NotNull
        @Size(min = 5, max = 30)
        private String password;
    }
}
