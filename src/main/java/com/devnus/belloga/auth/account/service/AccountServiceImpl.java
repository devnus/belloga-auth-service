package com.devnus.belloga.auth.account.service;

import com.devnus.belloga.auth.account.domain.Account;
import com.devnus.belloga.auth.account.domain.AuthProvider;
import com.devnus.belloga.auth.account.domain.CustomAccount;
import com.devnus.belloga.auth.account.dto.RequestAccount;
import com.devnus.belloga.auth.account.dto.ResponseAccount;
import com.devnus.belloga.auth.account.dto.EventAccount;
import com.devnus.belloga.auth.account.event.AccountProducer;
import com.devnus.belloga.auth.account.repository.AccountRepository;
import com.devnus.belloga.auth.common.aop.annotation.UserRole;
import com.devnus.belloga.auth.common.exception.error.DuplicateAccountException;
import com.devnus.belloga.auth.common.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

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
        if(accountRepository.existsByUsername(request.getEmail() + UserRole.ENTERPRISE)) {
            throw new DuplicateAccountException("아이디가 중복되었습니다");
        }
        Account account = (Account) accountRepository.save(CustomAccount.builder()
                .id(SecurityUtil.encryptSHA256(request.getEmail() + AuthProvider.CUSTOM))//여기에 해쉬값 추가
                .username(request.getEmail() + AuthProvider.CUSTOM)
                .password(SecurityUtil.encryptSHA256(request.getPassword()))//여기에 암호화 추가
                .isLocked(false)
                .build());

        // save-account-enterprise 토픽으로 보내기
        EventAccount.RegisterAccountEnterprise event = EventAccount.RegisterAccountEnterprise.builder()
                .accountId(account.getId())
                .email(request.getEmail())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .organization(request.getOrganization())
                .userRole(UserRole.ENTERPRISE)
                .build();
        accountProducer.registerAccountEnterprise(event);

        return ResponseAccount.RegisterAccount.builder()
                .userRole(UserRole.ENTERPRISE)
                .build();
    }

    /**
     * 자체 로그인 정보를 통해 회원 인증 후 account_id 반환
     */
    @Override
    public String authenticateCustomAccount(RequestAccount.SignInCustomAccount request) {

        //계정 존재 확인
        Optional<Account> account = accountRepository.findByUsername(request.getEmail() + AuthProvider.CUSTOM);
        CustomAccount customAccount = (CustomAccount) account.orElseThrow(()->new AuthenticationCredentialsNotFoundException("계정이 존재하지 않습니다"));

        //비밀번호 매치 확인
        if(!customAccount.getPassword().equals(SecurityUtil.encryptSHA256(request.getPassword()))){
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
        }

        return customAccount.getId();
    }
}
