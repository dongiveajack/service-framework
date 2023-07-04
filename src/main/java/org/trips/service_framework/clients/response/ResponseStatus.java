package org.trips.service_framework.clients.response;

import lombok.Data;

/**
 * @author Anupam Dagar on 26/10/22
 */
@Data
public class ResponseStatus {
    private Integer code;
    private String message;
    private String type;
}
