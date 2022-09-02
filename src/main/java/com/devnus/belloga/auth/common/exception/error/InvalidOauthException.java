package com.devnus.belloga.auth.common.exception.error;

public class InvalidOauthException extends RuntimeException {
    public InvalidOauthException() {
        super();
    }
    public InvalidOauthException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidOauthException(String message) {
        super(message);
    }
    public InvalidOauthException(Throwable cause) {
        super(cause);
    }
}