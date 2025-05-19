package org.trips.service_framework.events;

import com.amazonaws.services.sns.model.PublishRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.trips.service_framework.configs.SnsConfig;
import org.trips.service_framework.events.dto.SkuCreationTopicMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnsEventPublisher {

    private final SnsConfig snsConfig;

    private final ObjectMapper objectMapper;

    @Value("${sns.topic.sku-creation}")
    private String skuCreationTopic;

    @SneakyThrows
    public String publishToSkuCreationTopic(SkuCreationTopicMessage message) {
        log.info("Publishing SKU Creation Message: {} for topic: {}", message, skuCreationTopic);

        String serialisedMessage = objectMapper.writeValueAsString(message);

        log.info("SKU Creation Serialised Message: {}", serialisedMessage);

        PublishRequest request = new PublishRequest()
                .withMessage(serialisedMessage)
                .withTopicArn(skuCreationTopic);

        return snsConfig.getClient()
                .publish(request)
                .getMessageId();
    }
}