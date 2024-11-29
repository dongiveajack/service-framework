package org.trips.service_framework.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.trips.service_framework.utils.CmsUtils;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuAttributes {
    @NotEmpty(message = "Spec is required")
    private String spec;
    @NotEmpty(message = "Species is required")
    private String species;
    @NotEmpty(message = "Product type is required")
    private String productType;
    private String quality;
    private String freezingMethod;
    private String packing;
    private String glazingPercentage;
    private String unitWeight;
    private String treatment;
    private String quantityPerUnit;
    private String unitPerCarton;
    private String catchType;
    private String grade;
    private String certification;

    public static Map<String, Function<SkuAttributes, Object>> attributeGetters() {
        Map<String, Function<SkuAttributes, Object>> attributeGetters = new HashMap<>() {{
            put("spec", SkuAttributes::getSpec);
            put("species", SkuAttributes::getSpecies);
            put("product_type", SkuAttributes::getProductType);
            put("freezing_method", SkuAttributes::getFreezingMethod);
            put("packing", SkuAttributes::getPacking);
            put("catch_type", SkuAttributes::getCatchType);
            put("glazing_percentage", SkuAttributes::getGlazingPercentage);
            put("unit_weight", SkuAttributes::getUnitWeight);
            put("quantity_per_unit", SkuAttributes::getQuantityPerUnit);
            put("unit_per_carton", SkuAttributes::getUnitPerCarton);
            put("treatment", SkuAttributes::getTreatment);
            put("grade", SkuAttributes::getGrade);
            put("quality", SkuAttributes::getQuality);
            put("certification", SkuAttributes::getCertification);
        }};

        return attributeGetters;
    }
}

