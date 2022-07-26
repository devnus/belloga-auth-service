package com.devnus.belloga.auth.common.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS;

    @Value(value = "${app.topic.account.register-account-enterprise}")
    private String REGISTER_ACCOUNT_ENTERPRISE_TOPIC;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        return new KafkaAdmin(configs);
    }

    /**
     * 기업사용자가 자체 회원가입을 했을때 사용하는 카프카의 토픽 생성
     * 같은 이름의 토픽이 등록되어 있다면 동작하지 않음
     * partition 개수, replica 개수는 논의 후 수정
     */
    @Bean
    public NewTopic registerCustomAccountEnterpriseTopic() {
        return TopicBuilder.name(REGISTER_ACCOUNT_ENTERPRISE_TOPIC)
                .partitions(1)
                .replicas(1)
                .compact()
                .build();
    }
}
