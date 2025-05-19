package org.trips.service_framework.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.trips.service_framework.constants.CmsConstants;
import org.trips.service_framework.dtos.SkuAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CmsUtils {

    public static String sanitizeInput(String attribute, String value) {
        if (isNull(value)) {
            return CmsConstants.NOT_APPLICABLE;
        }

        if (attribute.equals("treatment")) {
            return sanitizeTreatment(value);
        }

        return value.trim();
    }

    private static String sanitizeTreatment(String treatment) {
        List<String> treatmentArray = new ArrayList<>();
        Arrays.stream(treatment.split(","))
                .map(CmsUtils::reformatTreatmentString)
                .filter(trt -> StringUtils.isNotEmpty(trt))
                .forEach(trt -> treatmentArray.add(String.format("\"%s\"", trt)));

        if (CollectionUtils.isEmpty(treatmentArray)) {
            return CmsConstants.NOT_APPLICABLE;
        }

        String result = StringUtils.join(treatmentArray, ",");
        return String.format("[%s]", result);
    }

    private static String reformatTreatmentString(String treatment) {
        treatment = treatment.replace("[", "").replace("]", "");
        treatment = treatment.replaceAll("\"", "");
        return treatment.trim();
    }

    public static int getDefaultShelfLife(String freezingMethod) {
        if (CmsConstants.FREEZING_METHOD_FRESH.equals(freezingMethod)) {
            return CmsConstants.RAW_MATERIAL_EXPIRY_DAYS;
        }

        return CmsConstants.DEFAULT_EXPIRY_DAYS;
    }

    public static SkuAttributes enrichQuantityAttributes(SkuAttributes skuAttributes) {
        String unitPerCarton = skuAttributes.getUnitPerCarton();
        String quantityPerUnit = skuAttributes.getQuantityPerUnit();
        String unitWeight = skuAttributes.getUnitWeight();

        skuAttributes.setUnitPerCarton(CmsConstants.NOT_APPLICABLE);
        skuAttributes.setQuantityPerUnit(CmsConstants.NOT_APPLICABLE);
        skuAttributes.setUnitWeight(CmsConstants.NOT_APPLICABLE);

        if (nonNull(unitPerCarton) && nonNull(quantityPerUnit)) {
            skuAttributes.setUnitPerCarton(unitPerCarton);
            skuAttributes.setQuantityPerUnit(quantityPerUnit);
        } else if (nonNull(unitWeight)) {
            skuAttributes.setUnitWeight(unitWeight);
        }

        return skuAttributes;
    }

    public static boolean isNull(String str) {
        return StringUtils.isEmpty(str) || CmsConstants.NOT_APPLICABLE.equals(str);
    }

    public static boolean nonNull(String str) {
        return !isNull(str);
    }
}
