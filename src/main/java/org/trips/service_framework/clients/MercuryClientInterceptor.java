package org.trips.service_framework.clients;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.trips.service_framework.utils.Constants;
import org.trips.service_framework.utils.Context;

public class MercuryClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        template.header(Constants.NAMESPACE_ID_HEADER, Context.getNamespaceId());
    }
}
