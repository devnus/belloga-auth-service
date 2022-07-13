package com.devnus.belloga.auth.account.adapter.out.persistence;

import com.devnus.belloga.auth.account.application.port.out.SaveEnterprisePort;
import com.devnus.belloga.auth.account.domain.Enterprise;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountPersistenceAdapter implements SaveEnterprisePort {

    private final AccountSpringDataRepository accountSpringDataRepository;

    @Override
    public void saveEnterprise(Enterprise enterprise) {

    }
}
