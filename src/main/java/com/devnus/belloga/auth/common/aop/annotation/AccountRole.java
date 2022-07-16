package com.devnus.belloga.auth.common.aop.annotation;

import lombok.Getter;

@Getter
public enum AccountRole {
    ENTERPRISE("enterprise-id"),
    ADMIN("admin-id"),
    LABELER("labeler-id");

    private String key;

    AccountRole(final String key) {
        this.key = key;
    }
}
