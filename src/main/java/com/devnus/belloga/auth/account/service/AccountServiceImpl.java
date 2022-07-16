package com.devnus.belloga.auth.account.service;

import com.devnus.belloga.auth.account.domain.Account;
import com.devnus.belloga.auth.account.domain.CustomAccount;
import com.devnus.belloga.auth.account.dto.RequestAccount;
import com.devnus.belloga.auth.account.dto.ResponseAccount;
import com.devnus.belloga.auth.account.event.EventAccount;
import com.devnus.belloga.auth.account.repository.AccountRepository;
import com.devnus.belloga.auth.common.aop.annotation.AccountRole;
import com.devnus.belloga.auth.common.exception.error.DuplicateAccountException;
import com.devnus.belloga.auth.common.exception.error.EncryptException;
import com.devnus.belloga.auth.common.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final KafkaProducer kafkaProducer;

    @Value("${app.topic-one}")
    private String topicOne;

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
                .id(encrypt(request.getEmail() + AccountRole.ENTERPRISE))//여기에 해쉬값 추가
                .username(request.getEmail() + AccountRole.ENTERPRISE)
                .password(encrypt(request.getPassword()))//여기에 암호화 추가
                .accountRole(AccountRole.ENTERPRISE)
                .isLocked(false)
                .build());

        // save-custom-account-enterprise 토픽으로 보내기
        EventAccount.saveCustomAccountEnterprise event = EventAccount.saveCustomAccountEnterprise.builder()
                .account_id(account.getId())
                .email(request.getEmail())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .organization(request.getOrganization())
                .accountRole(account.getAccountRole())
                .build();
        kafkaProducer.send(topicOne, event);

        return ResponseAccount.RegisterAccount.of(account);
    }

    /**
     * SHA-256으로 암호화
     */
    @Override
    public String encrypt(String text) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());

            byte[] bytes = md.digest();
            StringBuilder builder = new StringBuilder();
            for(byte b : bytes) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException("지원하지 않는 암호화 알고리즘 사용",e);
        }
    }
}
