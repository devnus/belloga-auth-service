package com.devnus.belloga.auth.account.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value(value = "${app.topic.account.register-enterprise}")
    private String REGISTER_ENTERPRISE_TOPIC;
    @Value(value = "${app.topic.account.register-labeler}")
    private String REGISTER_LABELER_TOPIC;

    public void registerEnterprise(Object event) {
        kafkaTemplate.send(REGISTER_ENTERPRISE_TOPIC, event);
    }

    public void registerLabeler(Object event) {
        kafkaTemplate.send(REGISTER_LABELER_TOPIC, event);
    }
}
