package com.devnus.belloga.auth.account.dto;

import com.devnus.belloga.auth.common.aop.annotation.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ResponseUser {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private String userId;
        private UserRole userRole;
    }
}
