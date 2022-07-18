package com.devnus.belloga.auth.account.service;

import com.devnus.belloga.auth.account.dto.RequestAccount;
import com.devnus.belloga.auth.account.dto.ResponseAccount;

public interface AccountService {
    ResponseAccount.RegisterAccount saveCustomAccountEnterprise(RequestAccount.RegisterCustomAccountEnterprise request);
    String authenticateCustomAccount(RequestAccount.SignInCustomAccount request);
}
