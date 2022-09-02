package com.devnus.belloga.auth.account.service;

import com.devnus.belloga.auth.account.domain.AuthProvider;
import com.devnus.belloga.auth.account.dto.RequestAccount;
import com.devnus.belloga.auth.account.dto.ResponseAccount;
import com.devnus.belloga.auth.account.dto.ResponseOauth;

public interface AccountService {
    String authenticateCustomAccount(RequestAccount.SignInCustomAccount request);
    ResponseAccount.RegisterAccount saveCustomAccount(RequestAccount.RegisterCustomAccountEnterprise request);
    String authenticateOauthAccount(ResponseOauth.UserInfo userInfo, AuthProvider authProvider);
    String saveOauthAccount(ResponseOauth.UserInfo userInfo, AuthProvider authProvider);
}
