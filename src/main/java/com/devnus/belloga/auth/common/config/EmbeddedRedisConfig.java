package com.devnus.belloga.auth.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * Embedded Redis의 Start 및 Stop 설정
 */
@Configuration
@Profile({"test"})
public class EmbeddedRedisConfig {
    private RedisServer redisServer;
    @Value("${spring.redis.port}")
    private int redisPort;

    @PostConstruct
    public void startRedis() throws IOException {
        redisServer = new RedisServer(redisPort);
        redisServer.start(); //Redis 시작
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop(); //Redis 종료
    }
}
