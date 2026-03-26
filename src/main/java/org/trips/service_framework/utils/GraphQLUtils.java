package org.trips.service_framework.utils;

import org.springframework.stereotype.Component;
import org.trips.service_framework.constants.CmsConstants;

@Component
public class GraphQLUtils {

    public String getQueryFilePath(String filename) {
        return String.join("/", CmsConstants.QUERY_FOLDER_PATH, filename);
    }
}

