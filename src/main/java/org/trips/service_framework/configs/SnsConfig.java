package org.trips.service_framework.configs;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Getter
@Component
public class SnsConfig {

    private AmazonSNS client;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    private void initialize() {
        this.client = AmazonSNSClientBuilder
                .standard()
                .withRegion(region)
                .build();
    }
}
