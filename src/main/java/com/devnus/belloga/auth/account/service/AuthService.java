package com.devnus.belloga.auth.account.service;

import com.devnus.belloga.auth.account.dto.ResponseAuth;
import com.devnus.belloga.auth.common.aop.annotation.UserRole;

public interface AuthService {
    ResponseAuth.Token generateToken(String accountId, UserRole userRole);
    ResponseAuth.Token reissueToken(String refreshToken);
}
