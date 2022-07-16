package com.devnus.belloga.auth.account.dto;

import com.devnus.belloga.auth.account.domain.Account;
import com.devnus.belloga.auth.common.aop.annotation.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ResponseAccount {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterAccount {
        private String username;
        private AccountRole accountRole;

        public static ResponseAccount.RegisterAccount of(Account account) {
            if(account == null) return null;

            return RegisterAccount.builder()
                    .username(account.getUsername())
                    .accountRole(account.getAccountRole())
                    .build();
        }
    }
}
