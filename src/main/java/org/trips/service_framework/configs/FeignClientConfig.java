package org.trips.service_framework.configs;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.trips.service_framework.utils.Context;

/**
 * @author Abhinav Tripathi 16/12/22
 */
@Configuration
public class FeignClientConfig {

    @Value("${service.client-id}")
    private String clientId;

    @Value("${service.client-secret}")
    private String clientSecret;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Accept", "application/json");
            requestTemplate.header("saas-namespace", Context.getNamespaceId());
            requestTemplate.header("Client-Id", clientId);
            requestTemplate.header("Client-Secret", clientSecret);
        };
    }
}
