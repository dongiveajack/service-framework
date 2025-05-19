package org.trips.service_framework.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Optional;

@Data
@SuperBuilder
public class CmsSkuResponse {
    public ResponseBody data;

    @Data
    public static class SkuAttribute {
        public String value;
        public Attribute attribute;
    }

    @Data
    public static class Attribute {
        public String name;
    }

    @Data
    @Builder
    public static class ResponseBody {
        public List<Sku> searchSkus;
    }

    @Data
    public static class ProductType {
        public String id;
        public String name;
    }

    @Data
    public static class Sku {
        public String id;
        public String name;
        public String code;
        public List<SkuAttribute> attributes;

        @JsonProperty("productType")
        public ProductType productType;

        @JsonProperty("isActive")
        public boolean isActive;

        public Optional<String> getShelfLife() {
            return Optional.ofNullable(this.attributes).stream()
                    .flatMap(List::stream)
                    .filter(skuAttribute -> skuAttribute.getAttribute().getName().equals("shelf_life"))
                    .map(SkuAttribute::getValue)
                    .findFirst();
        }
    }
}