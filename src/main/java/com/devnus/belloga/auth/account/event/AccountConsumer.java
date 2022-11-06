package com.devnus.belloga.auth.account.event;


import com.devnus.belloga.auth.account.dto.EventAccount;
import com.devnus.belloga.auth.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.KafkaListener;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class AccountConsumer {
    private static final Logger logger = LoggerFactory.getLogger(AccountConsumer.class);
    private final AccountService accountService;
    /**
     * 기업 사용자 등록 후 사가 패턴으로 응답을 받아 보상 트랜잭션 처리
     * @param event
     * @throws IOException
     */
    @KafkaListener(topics = "register-enterprise-saga", groupId = "register-enterprise-saga-1", containerFactory = "eventRegisterEnterpriseSagaPatternListener")
    protected void consumeRegisterEnterpriseSagaPattern(EventAccount.RegisterEnterpriseSaga event) throws IOException {
        if(!event.isSuccess()) {
            // 등록 실패 시 보상 트랜잭션
            accountService.deleteRegisteredAccount(event.getAccountId());
            logger.info(event.getAccountId() + " Saga 보상 트랜잭션 발행");
        } else {
            logger.info(event.getAccountId() + " 등록 성공(Saga)");
        }
    }
    /**
     * 일반 사용자 등록 후 사가 패턴으로 응답을 받아 보상 트랜잭션 처리
     * @param event
     * @throws IOException
     */
    @KafkaListener(topics = "register-labeler-saga", groupId = "register-labeler-saga-1", containerFactory = "eventRegisterLabelerSagaPatternListener")
    protected void consumeSuccessLabelerLabeledData(EventAccount.RegisterLabelerSaga event) throws IOException {
        if(!event.isSuccess()) {
            // 등록 실패 시 보상 트랜잭션
            accountService.deleteRegisteredAccount(event.getAccountId());
            logger.info(event.getAccountId() + " Saga 보상 트랜잭션 발행");
        } else {
            logger.info(event.getAccountId() + " 등록 성공(Saga)");
        }
    }

    /**
     * 관리자 등록 후 사가 패턴으로 응답을 받아 보상 트랜잭션 처리
     * @param event
     * @throws IOException
     */
    @KafkaListener(topics = "register-admin-saga", groupId = "register-admin-saga-1", containerFactory = "eventRegisterAdminSagaPatternListener")
    protected void consumeRegisterAdminSagaPattern(EventAccount.RegisterAdminSaga event) throws IOException {
        if(!event.isSuccess()) {
            // 등록 실패 시 보상 트랜잭션
            accountService.deleteRegisteredAccount(event.getAccountId());
            logger.info(event.getAccountId() + " Saga 보상 트랜잭션 발행");
        } else {
            logger.info(event.getAccountId() + " 등록 성공(Saga)");
        }
    }
}
