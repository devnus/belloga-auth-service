package com.devnus.belloga.auth.common.exception;

import com.devnus.belloga.auth.common.dto.CommonResponse;
import com.devnus.belloga.auth.common.dto.ErrorResponse;
import com.devnus.belloga.auth.common.exception.error.DuplicateAccountException;
import com.devnus.belloga.auth.common.exception.error.EncryptException;
import com.devnus.belloga.auth.common.exception.error.InvalidOauthException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 회원가입 시, 회원 계정 정보가 중복 되었을때
     */
    @ExceptionHandler(DuplicateAccountException.class)
    protected ResponseEntity<CommonResponse> handleDuplicateAccountException(DuplicateAccountException ex) {
        ErrorCode errorCode = ErrorCode.DUPLICATE_ACCOUNT_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    /**
     * 계정 주요 정보의 암호화 과정이 실패 했을때
     */
    @ExceptionHandler(EncryptException.class)
    protected ResponseEntity<CommonResponse> handleEncryptException(EncryptException ex) {
        ErrorCode errorCode = ErrorCode.ENCRYPT_FAILED_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    /**
     * 리퀘스트 파라미터 바인딩이 실패했을때
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<CommonResponse> handleRequestParameterBindException(BindException ex) {
        ErrorCode errorCode = ErrorCode.REQUEST_PARAMETER_BIND_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    /**
     * 사용자 인증이 실패했을때
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<CommonResponse> handleAuthenticationException(AuthenticationException ex) {
        ErrorCode errorCode = ErrorCode.AUTHENTICATION_FAILED_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    /**
     * 유효하지 않는 Oauth 정보일때
     */
    @ExceptionHandler(InvalidOauthException.class)
    protected ResponseEntity<CommonResponse> handleInvalidOauthException(InvalidOauthException ex) {
        ErrorCode errorCode = ErrorCode.OAUTH_AUTHENTICATION_FAILED_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }


}
