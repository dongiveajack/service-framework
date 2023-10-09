package org.trips.service_framework.clients.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.trips.service_framework.codes.ErrorCodes;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private ResponseStatus status;

    public static NotificationResponse getErrorStatus(String message) {
        return NotificationResponse.builder()
                .status(ResponseStatus.builder()
                        .code(ErrorCodes.BAD_REQUEST.getCode())
                        .message(message)
                        .type("Error")
                        .build())
                .build();
    }
}
