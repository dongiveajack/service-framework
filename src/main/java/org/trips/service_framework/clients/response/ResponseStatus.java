package org.trips.service_framework.clients.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Anupam Dagar on 26/10/22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStatus {
    private Integer code;
    private String message;
    private String type;
}
