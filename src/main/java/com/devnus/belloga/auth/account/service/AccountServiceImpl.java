package com.devnus.belloga.auth.account.service;

import com.devnus.belloga.auth.account.domain.Account;
import com.devnus.belloga.auth.account.domain.AuthProvider;
import com.devnus.belloga.auth.account.domain.CustomAccount;
import com.devnus.belloga.auth.account.domain.OauthAccount;
import com.devnus.belloga.auth.account.dto.*;
import com.devnus.belloga.auth.account.event.AccountProducer;
import com.devnus.belloga.auth.account.repository.AccountRepository;
import com.devnus.belloga.auth.common.aop.annotation.UserRole;
import com.devnus.belloga.auth.common.exception.error.DuplicateAccountException;
import com.devnus.belloga.auth.common.exception.error.NotFoundAccountException;
import com.devnus.belloga.auth.common.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ResponseAccount.RegisterAccount saveCustomAccount(RequestAccount.RegisterCustomAccountEnterprise request) {

        //중복 아이디 확인
        if(accountRepository.existsByUsername(request.getEmail() + AuthProvider.CUSTOM)) {
            throw new DuplicateAccountException("아이디가 중복되었습니다");
        }
        Account account = (Account) accountRepository.save(CustomAccount.builder()
                .id(SecurityUtil.encryptSHA256(request.getEmail() + AuthProvider.CUSTOM))//여기에 해쉬값 추가
                .username(request.getEmail() + AuthProvider.CUSTOM)
                .password(SecurityUtil.encryptSHA256(request.getPassword()))//여기에 암호화 추가
                .isLocked(false)
                .userRole(UserRole.ENTERPRISE)
                .build());

        // register-enterprise 토픽으로 보내기
        EventAccount.RegisterEnterprise event = EventAccount.RegisterEnterprise.builder()
                .accountId(account.getId())
                .email(request.getEmail())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .organization(request.getOrganization())
                .build();
        accountProducer.registerEnterprise(event);

        return ResponseAccount.RegisterAccount.builder()
                .userRole(account.getUserRole())
                .build();
    }

    /**
     * 자체 계정 로그인을 통해 회원 인증 후 accountId와 userRole 반환
     */
    @Override
    public ResponseAccount.SignInAccount authenticateCustomAccount(RequestAccount.SignInCustomAccount request) {

        //계정 존재 확인
        Optional<Account> account = accountRepository.findByUsername(request.getEmail() + AuthProvider.CUSTOM);
        CustomAccount customAccount = (CustomAccount) account.orElseThrow(()->new AuthenticationCredentialsNotFoundException("계정이 존재하지 않습니다"));

        //비밀번호 매치 확인
        if(!customAccount.getPassword().equals(SecurityUtil.encryptSHA256(request.getPassword()))){
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
        }

        return ResponseAccount.SignInAccount.builder()
                .accountId(customAccount.getId())
                .userRole(customAccount.getUserRole())
                .build();
    }

    /**
     * Oauth 고유 id 인증 후 accountId와 userRole 반환
     * 저장되지 않은 Oauth 고유 id 값이면 회원가입 후 accountId와 userRole 반환
     */
    @Override
    public ResponseAccount.SignInAccount authenticateOauthAccount(ResponseOauth.UserInfo userInfo, AuthProvider authProvider) {

        /**
         * 기존에 존재하지 않는 Oauth 고유 id 일때 회원가입 진행 후 accountId와 userRole 반환
         */
        if(!accountRepository.existsByUsername(userInfo.getId() + authProvider)){
            return saveOauthAccount(userInfo, authProvider);
        }

        Optional<Account> account = accountRepository.findByUsername(userInfo.getId() + authProvider);
        OauthAccount oauthAccount = (OauthAccount) account.orElseThrow(()->new AuthenticationCredentialsNotFoundException("계정이 존재하지 않습니다"));

        return ResponseAccount.SignInAccount.builder()
                .accountId(oauthAccount.getId())
                .userRole(oauthAccount.getUserRole())
                .build();
    }

    /**
     * 회원가입후 accountId와 userRole 반환
     */
    @Override
    @Transactional
    public ResponseAccount.SignInAccount saveOauthAccount(ResponseOauth.UserInfo userInfo, AuthProvider authProvider){

        //중복 아이디 확인
        if(accountRepository.existsByUsername(userInfo.getId() + authProvider)) {
            throw new DuplicateAccountException("이미 아이디가 존재합니다");
        }

        OauthAccount oauthAccount = (OauthAccount) accountRepository.save(OauthAccount.builder()
                .id(SecurityUtil.encryptSHA256(userInfo.getId() + authProvider))//여기에 해쉬값 추가
                .username(userInfo.getId() + authProvider)
                .isLocked(false)
                .authProvider(authProvider)
                .userRole(UserRole.LABELER)
                .build());

        //register-labeler 토픽으로 보내기
        EventAccount.RegisterLabeler event = EventAccount.RegisterLabeler.builder()
                .accountId(oauthAccount.getId())
                .birthYear(userInfo.getBirthYear())
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .phoneNumber(userInfo.getPhoneNumber())
                .build();
        accountProducer.registerLabeler(event);

        return ResponseAccount.SignInAccount.builder()
                .accountId(oauthAccount.getId())
                .userRole(oauthAccount.getUserRole())
                .build();
    }

    /**
     * 보상 트랜잭션으로 계정을 삭제한다.
     * @param accountId
     * @return
     */
    @Transactional
    @Override
    public boolean deleteRegisteredAccount(String accountId) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        Account account = optionalAccount.orElseThrow(()->new NotFoundAccountException());

        accountRepository.delete(account);
        return true;
    }
}
