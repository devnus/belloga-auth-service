package com.devnus.belloga.auth.account.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountSpringDataRepository<T extends AccountJpaEntity> extends JpaRepository<T, String> {
}
