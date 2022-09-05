package com.devnus.belloga.auth.account.service;

import com.devnus.belloga.auth.account.domain.AuthProvider;
import com.devnus.belloga.auth.account.dto.RequestAccount;
import com.devnus.belloga.auth.account.dto.ResponseAccount;
import com.devnus.belloga.auth.account.dto.ResponseOauth;

public interface AccountService {
    ResponseAccount.SignInAccount authenticateCustomAccount(RequestAccount.SignInCustomAccount request);
    ResponseAccount.RegisterAccount saveCustomAccount(RequestAccount.RegisterCustomAccountEnterprise request);
    ResponseAccount.SignInAccount authenticateOauthAccount(ResponseOauth.UserInfo userInfo, AuthProvider authProvider);
    ResponseAccount.SignInAccount saveOauthAccount(ResponseOauth.UserInfo userInfo, AuthProvider authProvider);
}
