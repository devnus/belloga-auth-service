package com.devnus.belloga.auth.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RequestAuth {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Reissue {
        private String refreshToken;
    }
}
