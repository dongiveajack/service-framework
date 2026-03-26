package org.trips.service_framework.audit.responses;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.trips.service_framework.audit.dtos.AuditEntry;
import org.trips.service_framework.models.responses.BaseResponse;

import java.util.List;

/**
 * @author anomitra on 20/12/24
 */

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class AuditResponse extends BaseResponse {
    private List<AuditEntry> auditDetails;
}

