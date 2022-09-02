package com.devnus.belloga.auth.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${app.web-client.base-url}")
    private String BASE_URL;
    @Value("${app.web-client.base-url-naver}")
    private String BASE_URL_NAVER;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(BASE_URL)
                .build();

    }

    @Bean(name = "naverWebClient")
    public WebClient naverWebClient() {
        return WebClient.builder()
                .baseUrl(BASE_URL_NAVER)
                .build();
    }
}
