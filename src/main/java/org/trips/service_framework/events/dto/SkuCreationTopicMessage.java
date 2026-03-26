package org.trips.service_framework.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.trips.service_framework.utils.Context;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuCreationTopicMessage {
    private String skuCode;
    private String namespaceId;

    public static SkuCreationTopicMessage of(String skuCode) {
        return new SkuCreationTopicMessage(skuCode, Context.getNamespaceId());
    }
}

