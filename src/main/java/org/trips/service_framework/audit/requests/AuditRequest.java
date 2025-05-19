package org.trips.service_framework.audit.requests;

import lombok.Data;

/**
 * @author anomitra on 20/12/24
 */

@Data
public class AuditRequest {
    private Long entityId;
    private String entityClass;
}

