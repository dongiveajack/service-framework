package org.trips.service_framework.audit.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author anomitra on 20/12/24
 */
@Data
@Builder
public class AuditEntry {
    private String commitId;
    private String commitAuthor;
    private LocalDateTime commitDate;
    private String entityClass;
    private Long entityId;
    private List<ChangeDetail> changes;
}
