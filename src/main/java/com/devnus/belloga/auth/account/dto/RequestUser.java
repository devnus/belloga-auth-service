package com.devnus.belloga.auth.account.dto;

import com.devnus.belloga.auth.common.aop.annotation.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RequestUser {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterOauthUser {
        private String accountId;
        private UserRole userRole;
        private String email;
        private String name;
        private String phoneNumber;
        private String birthYear;
    }
}
