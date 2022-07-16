package com.devnus.belloga.auth.account.service;

import com.devnus.belloga.auth.account.domain.Account;
import com.devnus.belloga.auth.account.dto.RequestAccount;
import com.devnus.belloga.auth.account.dto.ResponseAccount;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    ResponseAccount.RegisterAccount saveCustomAccountEnterprise(RequestAccount.RegisterCustomAccountEnterprise request);
    String encrypt(String text);
}
