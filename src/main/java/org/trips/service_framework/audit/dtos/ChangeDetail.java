package org.trips.service_framework.audit.dtos;

import lombok.Builder;
import lombok.Data;

/**
 * @author anomitra on 20/12/24
 */
@Data
@Builder
public class ChangeDetail {
    String property;
    String changeType;
    Object left;
    Object right;
}
