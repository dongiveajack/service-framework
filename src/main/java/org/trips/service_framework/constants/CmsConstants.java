package org.trips.service_framework.constants;

import java.util.List;

public class CmsConstants {
    public static final String NOT_APPLICABLE = "NA";
    public static final String VERSION = "v2";
    public static final String PRODUCT_TYPE = "FAAS";
    public static final String QUERY_FOLDER_PATH = "queries";
    public static final List<String> ATTRIBUTES_NAMES = List.of("species", "product_type", "spec",
            "catch_type", "freezing_method", "packing", "glazing_percentage", "unit_weight",
            "quantity_per_unit", "unit_per_carton", "treatment", "grade", "quality", "certification");
    public static final int RAW_MATERIAL_EXPIRY_DAYS = 14;
    public static final int DEFAULT_EXPIRY_DAYS = 730;
    public static final String FREEZING_METHOD_FRESH = "Fresh";
}
