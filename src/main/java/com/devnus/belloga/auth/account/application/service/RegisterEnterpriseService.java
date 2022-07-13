package com.devnus.belloga.auth.account.application.service;

import com.devnus.belloga.auth.account.application.port.in.RegisterEnterpriseCommand;
import com.devnus.belloga.auth.account.application.port.in.RegisterEnterpriseUseCase;

public class RegisterEnterpriseService implements RegisterEnterpriseUseCase {

    @Override
    public void registerEnterprise(RegisterEnterpriseCommand command) {

        //중복 이메일 검사

        //이메일 인증

        //저장
    }
}
