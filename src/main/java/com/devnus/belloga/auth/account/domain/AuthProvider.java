package com.devnus.belloga.auth.account.domain;

/**
 * 인증을 제공하는 곳 (CUSTOM은 자체 인증 방식)
 */
public enum AuthProvider {
    CUSTOM,
    GOOGLE,
    KAKAO,
    NAVER
}
