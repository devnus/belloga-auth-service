package com.devnus.belloga.auth.account.dto;

import com.devnus.belloga.auth.account.domain.Account;
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

        public static ResponseAccount.RegisterAccount of(Account account) {
            if(account == null) return null;

            return RegisterAccount.builder()
                    .username(account.getUsername())
                    .build();
        }
    }
}
