package com.devnus.belloga.auth.account.application.port.out;

import com.devnus.belloga.auth.account.domain.Enterprise;

public interface SaveEnterprisePort {
    void saveEnterprise(Enterprise enterprise);
}
