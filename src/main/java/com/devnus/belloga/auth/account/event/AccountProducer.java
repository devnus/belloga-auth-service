package com.devnus.belloga.auth.account.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value(value = "${app.topic.account.register-custom-account-enterprise}")
    private String REGISTER_CUSTOM_ACCOUNT_ENTERPRISE_TOPIC;

    public void registerCustomAccountEnterprise(Object event) {
        kafkaTemplate.send(REGISTER_CUSTOM_ACCOUNT_ENTERPRISE_TOPIC, event);
    }
}
