package org.trips.service_framework.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.trips.service_framework.aop.Authenticate;
import org.trips.service_framework.dtos.CmsSearchRequestBody;
import org.trips.service_framework.dtos.CmsSkuResponse.Sku;
import org.trips.service_framework.dtos.SkuAttributes;
import org.trips.service_framework.helpers.CmsHelper;
import org.trips.service_framework.models.responses.StatusResponse;
import org.trips.service_framework.responses.CmsResponse;
import org.trips.service_framework.services.CmsService;

import java.util.*;

@RestController
@RequestMapping("/cms")
@RequiredArgsConstructor
public class CmsController {
    private final CmsService cmsService;
    private final CmsHelper cmsHelper;

    protected CmsResponse createResponse(List<Sku> skuList) {
        return CmsResponse.builder()
                .data(skuList)
                .status(StatusResponse.builder()
                        .statusCode(HttpStatus.SC_OK)
                        .statusType(StatusResponse.Type.SUCCESS)
                        .totalCount(skuList.size())
                        .build())
                .build();
    }

    @Authenticate
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public CmsResponse skuSearch(@RequestBody CmsSearchRequestBody body) {
        return createResponse(cmsService.skuSearch(body));
    }

    @Authenticate
    @RequestMapping(value = "/find-or-create", method = RequestMethod.POST)
    public CmsResponse findOrCreateSku(@RequestBody SkuAttributes body) {
        return createResponse(Collections.singletonList(cmsService.getOrCreateSku(body)));
    }
}
