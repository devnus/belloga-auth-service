package com.devnus.belloga.auth.account.dto;

import com.devnus.belloga.auth.common.aop.annotation.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class EventAccount {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class registerCustomAccountEnterprise {
        private String account_id;
        private String email;
        private String name;
        private String phoneNumber;
        private String organization;;
        private AccountRole accountRole;
    }
}