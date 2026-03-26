package org.trips.service_framework.helpers;

import org.springframework.stereotype.Service;
import org.trips.service_framework.constants.CmsConstants;
import org.trips.service_framework.dtos.SkuAttributes;
import org.apache.commons.text.StringSubstitutor;
import org.trips.service_framework.utils.CmsUtils;

import java.util.*;

@Service
public class CmsHelper {

    public SkuAttributes getAttributesFromMap(Map<String, String> attributeMap) {
        return SkuAttributes.builder()
                .species(attributeMap.get("species"))
                .productType(attributeMap.get("product_type"))
                .spec(attributeMap.get("spec"))
                .catchType(attributeMap.get("catch_type"))
                .freezingMethod(attributeMap.get("freezing_method"))
                .packing(attributeMap.get("packing"))
                .glazingPercentage(attributeMap.get("glazing_percentage"))
                .unitWeight(attributeMap.get("unit_weight"))
                .quantityPerUnit(attributeMap.get("quantity_per_unit"))
                .unitPerCarton(attributeMap.get("unit_per_carton"))
                .treatment(attributeMap.get("treatment"))
                .grade(attributeMap.get("grade"))
                .quality(attributeMap.get("quality"))
                .certification(attributeMap.get("certification"))
                .build();
    }

    public Map<String, Object> createRequestMap(String name, String value, String operator, boolean isAttribute) {
        return Map.of(
                "name", name,
                "value", CmsUtils.sanitizeInput(name, value),
                "operator", operator,
                "isAttribute", isAttribute
        );
    }

    public Map<String, Object> getSearchQueryFromSkuCodes(Collection<String> codes) {
        Map<String, Object> requestMap = Map.of(
                "value", codes,
                "name", "code",
                "operator", "IN",
                "isAttribute", false
        );

        return Map.of("searchQuery", Map.of("filters", List.of(requestMap)));
    }

    public Map<String, Object> getSearchQueryFromAttributes(SkuAttributes skuAttributes) {
        List<Map<String, Object>> attributeList = new ArrayList<>();
        CmsConstants.ATTRIBUTES_NAMES.forEach(attribute -> {
            String value = (String) SkuAttributes.attributeGetters().get(attribute).apply(skuAttributes);
            attributeList.add(createRequestMap(attribute, value, "EQ", true));
        });

        return Map.of("searchQuery", Map.of("filters", attributeList));
    }

    public Map<String, Object> getQueryForSkusSearch(SkuAttributes skuAttributes) {
        List<Map<String, Object>> attributeList = new ArrayList<>();
        CmsConstants.ATTRIBUTES_NAMES.forEach(attribute -> {
            String value = (String) SkuAttributes.attributeGetters().get(attribute).apply(skuAttributes);
            String sanitizeValue = CmsUtils.sanitizeInput(attribute, value);
            if (CmsUtils.nonNull(sanitizeValue)) {
                attributeList.add(createRequestMap(attribute, sanitizeValue, "EQ", true));
            }
        });

        return Map.of("searchQuery", Map.of("filters", attributeList));
    }

    public String constructSkuName(List<Map<String, String>> attributes) {
        Map<String, String> attributeMap = new HashMap<>();
        attributes.forEach(x -> {
            String name = x.get("name");
            String value = x.get("value");
            attributeMap.put(name, Objects.equals(value, CmsConstants.NOT_APPLICABLE) ? "" : value.trim());
        });

        String quantityPerUnit = attributeMap.getOrDefault("quantity_per_unit", "");
        String unitPerCarton = attributeMap.getOrDefault("unit_per_carton", "");
        String unitData = (quantityPerUnit.isEmpty() || unitPerCarton.isEmpty()) ? "" : String.format("%sx%s", unitPerCarton, quantityPerUnit);
        attributeMap.put("unitData", unitData);

        String glazingPercentage = attributeMap.get("glazing_percentage");
        if (!glazingPercentage.isEmpty()) {
            attributeMap.put("glazing_percentage", glazingPercentage + "% Glazing");
        }

        String skuNameTemplate = "${species} ${catch_type} ${product_type} ${spec} ${grade} ${quality} ${freezing_method} ${treatment} ${glazing_percentage} ${packing} ${unitData} ${unit_weight} ${certification}";
        return StringSubstitutor.replace(skuNameTemplate, attributeMap).replaceAll(" +", " ");
    }
}