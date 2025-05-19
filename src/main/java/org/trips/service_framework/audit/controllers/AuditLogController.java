package org.trips.service_framework.audit.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.trips.service_framework.audit.requests.AuditRequest;
import org.trips.service_framework.audit.responses.AuditResponse;
import org.trips.service_framework.audit.services.AuditLogService;
import org.trips.service_framework.models.responses.StatusResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author anomitra on 20/12/24
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/audit")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @PostMapping("/changes")
    public AuditResponse audit(@RequestBody AuditRequest request) {
        var data = auditLogService.getAuditLogs(request.getEntityId(), request.getEntityClass());
        return AuditResponse.builder()
                .auditDetails(data.getFirst())
                .userInfo(data.getSecond())
                .status(StatusResponse.builder()
                        .statusCode(HttpStatus.SC_OK)
                        .statusType(StatusResponse.Type.SUCCESS)
                        .totalCount(data.getFirst().size())
                        .build())
                .build();
    }

}
