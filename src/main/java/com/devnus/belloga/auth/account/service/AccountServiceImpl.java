package com.devnus.belloga.auth.account.service;

import com.devnus.belloga.auth.account.domain.Account;
import com.devnus.belloga.auth.account.domain.CustomAccount;
import com.devnus.belloga.auth.account.dto.RequestAccount;
import com.devnus.belloga.auth.account.dto.ResponseAccount;
import com.devnus.belloga.auth.account.dto.EventAccount;
import com.devnus.belloga.auth.account.event.AccountProducer;
import com.devnus.belloga.auth.account.repository.AccountRepository;
import com.devnus.belloga.auth.common.aop.annotation.AccountRole;
import com.devnus.belloga.auth.common.exception.error.DuplicateAccountException;
import com.devnus.belloga.auth.common.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountProducer accountProducer;

    /**
     * 기업 사용자의 자체 회원가입
     */
    @Override
    @Transactional
    public ResponseAccount.RegisterAccount saveCustomAccountEnterprise(RequestAccount.RegisterCustomAccountEnterprise request) {

        //중복 아이디 확인
        if(accountRepository.existsByUsername(request.getEmail() + AccountRole.ENTERPRISE)) {
            throw new DuplicateAccountException("아이디가 중복되었습니다");
        }
        Account account = (Account) accountRepository.save(CustomAccount.builder()
                .id(SecurityUtil.encryptSHA256(request.getEmail() + AccountRole.ENTERPRISE))//여기에 해쉬값 추가
                .username(request.getEmail() + AccountRole.ENTERPRISE)
                .password(SecurityUtil.encryptSHA256(request.getPassword()))//여기에 암호화 추가
                .accountRole(AccountRole.ENTERPRISE)
                .isLocked(false)
                .build());

        // save-custom-account-enterprise 토픽으로 보내기
        EventAccount.RegisterCustomAccountEnterprise event = EventAccount.RegisterCustomAccountEnterprise.builder()
                .account_id(account.getId())
                .email(request.getEmail())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .organization(request.getOrganization())
                .accountRole(account.getAccountRole())
                .build();
        accountProducer.registerCustomAccountEnterprise(event);

        return ResponseAccount.RegisterAccount.of(account);
    }
}
