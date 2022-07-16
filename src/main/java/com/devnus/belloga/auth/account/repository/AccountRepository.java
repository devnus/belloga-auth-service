package com.devnus.belloga.auth.account.repository;

import com.devnus.belloga.auth.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository<T extends Account> extends JpaRepository<T, String> {
    Optional<Account> findByUsername(String username);
    boolean existsByUsername(String username);
}
