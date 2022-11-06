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
    @Value("${spring.kafka.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS;
    @Value(value = "${app.topic.account.register-enterprise}")
    private String REGISTER_ENTERPRISE_TOPIC;
    @Value(value = "${app.topic.account.register-labeler}")
    private String REGISTER_LABELER_TOPIC;
    @Value(value = "${app.topic.account.register-admin}")
    private String REGISTER_ADMIN_TOPIC;
    @Value(value = "${app.topic.account.register-enterprise-saga}")
    private String REGISTER_ENTERPRISE_SAGA_TOPIC;
    @Value(value = "${app.topic.account.register-labeler-saga}")
    private String REGISTER_LABELER_SAGA_TOPIC;
    @Value(value = "${app.topic.account.register-admin-saga}")
    private String REGISTER_ADMIN_SAGA_TOPIC;

    /**
     * 기업 사용자 회원가입시 사용하는 카프카의 토픽 생성
     * 같은 이름의 토픽이 등록되어 있다면 동작하지 않음
     * partition 개수, replica 개수는 논의 후 수정
     */
    @Bean
    public NewTopic registerEnterpriseTopic() {
        return TopicBuilder.name(REGISTER_ENTERPRISE_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    /**
     * 일반 사용자 회원가입시 사용하는 카프카의 토픽 생성
     * 같은 이름의 토픽이 등록되어 있다면 동작하지 않음
     * partition 개수, replica 개수는 논의 후 수정
     */
    @Bean
    public NewTopic registerLabelerTopic() {
        return TopicBuilder.name(REGISTER_LABELER_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    /**
     * 관리자 회원가입시 사용하는 카프카의 토픽 생성
     * 같은 이름의 토픽이 등록되어 있다면 동작하지 않음
     * partition 개수, replica 개수는 논의 후 수정
     */
    @Bean
    public NewTopic registerAdminTopic() {
        return TopicBuilder.name(REGISTER_ADMIN_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    /**
     * 기업 사용자 등록 후 사가패턴 처리를 위한 토픽
     * @return
     */
    @Bean
    public NewTopic registerEnterpriseSagaTopic() {
        return TopicBuilder.name(REGISTER_ENTERPRISE_SAGA_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    /**
     * 일반 사용자 등록 후 사가패턴 처리를 위한 토픽
     * @return
     */
    @Bean
    public NewTopic registerLabelerSagaTopic() {
        return TopicBuilder.name(REGISTER_LABELER_SAGA_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    /**
     * 관리자 등록 후 사가패턴 처리를 위한 토픽
     */
    @Bean
    public NewTopic registerAdminSagaTopic() {
        return TopicBuilder.name(REGISTER_ADMIN_SAGA_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
