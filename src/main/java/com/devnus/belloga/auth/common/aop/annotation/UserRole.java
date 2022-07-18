package com.devnus.belloga.auth.common.aop.annotation;

import lombok.Getter;

@Getter
public enum UserRole {
    ENTERPRISE("enterprise-id"),
    ADMIN("admin-id"),
    LABELER("labeler-id");

    private String key;

    UserRole(final String key) {
        this.key = key;
    }
}
