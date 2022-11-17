package com.devnus.belloga.auth.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ResponseOauth {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private String id;
        private String birthYear;
        private String email;
        private String name;
    }
}
