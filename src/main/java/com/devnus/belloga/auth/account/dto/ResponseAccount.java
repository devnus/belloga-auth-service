package com.devnus.belloga.auth.account.dto;

import com.devnus.belloga.auth.account.domain.Account;
import com.devnus.belloga.auth.common.aop.annotation.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ResponseAccount {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterAccount { //회원가입 성공시 역할만 반환
        private UserRole userRole;
    }
}
