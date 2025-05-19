package org.trips.service_framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.trips.service_framework.enums.UnitOfMeasurement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Slf4j
public class UomUtils {

    private static final BigDecimal LbsToKgConversionFactor = BigDecimal.valueOf(0.45359237);
    private static final BigDecimal KgToLbsConversionFactor = BigDecimal.valueOf(2.20462262);

    public static BigDecimal convertLbsToKg(BigDecimal quantity) {
        return quantity.multiply(LbsToKgConversionFactor).setScale(3, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal convertKgToLbs(BigDecimal quantity) {
        return quantity.multiply(KgToLbsConversionFactor).setScale(3, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal convertQuantity(UnitOfMeasurement inputUom, UnitOfMeasurement outputUom, BigDecimal quantity) {
        if (Objects.isNull(inputUom) || Objects.isNull(outputUom)) {
            log.warn("Received one of the UOM as null. Returning same quantity. Input UOM: {}, OutputUom: {}", inputUom, outputUom);

            return quantity;
        }
        if (outputUom.equals(inputUom)) {
            return quantity;
        }
        if (outputUom.equals(UnitOfMeasurement.kg)) {
            return convertLbsToKg(quantity);
        }

        return convertKgToLbs(quantity);
    }
}
