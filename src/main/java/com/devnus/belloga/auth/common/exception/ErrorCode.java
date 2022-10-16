package com.devnus.belloga.auth.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    DUPLICATE_ACCOUNT_EXCEPTION(HttpStatus.CONFLICT, "ACCOUNT_001", "DUPLICATE_ACCOUNT"),
    ENCRYPT_FAILED_EXCEPTION(HttpStatus.NOT_ACCEPTABLE, "ACCOUNT_002", "ENCRYPT_FAILED"),
    AUTHENTICATION_FAILED_EXCEPTION(HttpStatus.UNAUTHORIZED, "ACCOUNT_003", "AUTHENTICATION_FAILED"),
    OAUTH_AUTHENTICATION_FAILED_EXCEPTION(HttpStatus.UNAUTHORIZED, "ACCOUNT_004", "OAUTH_AUTHENTICATION_FAILED"),
    INVALID_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "ACCOUNT_005","TOKEN_AUTHENTICATION_FAILED"),
    REQUEST_PARAMETER_BIND_EXCEPTION(HttpStatus.BAD_REQUEST, "REQ_001", "PARAMETER_BIND_FAILED");

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message){
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
