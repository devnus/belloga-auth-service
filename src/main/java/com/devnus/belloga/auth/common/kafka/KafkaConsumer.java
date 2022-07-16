package com.devnus.belloga.auth.common.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = "register-custom-account-enterprise", groupId = "register-custom-account-enterprise-1")
    protected void consume(Object event) throws IOException {
    }
}
