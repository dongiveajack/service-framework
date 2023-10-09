package org.trips.service_framework.notificationHandler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.trips.service_framework.clients.MercuryClient;
import org.trips.service_framework.clients.request.NotificationRequest;
import org.trips.service_framework.clients.response.NotificationResponse;
import org.trips.service_framework.dtos.SMSNotificationRequest;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SMSNotificationHandler implements NotificationHandler {
    private final MercuryClient mercuryClient;

    /**
     * calls the mercury service along with the request data.
     * returns error status if any exception.
     *
     * @param notificationRequest data required for the sms notification
     * @param <T>                 base class for the notification request
     * @return notification response from mercury service
     */
    @Override
    public <T extends NotificationRequest> NotificationResponse send(@NonNull T notificationRequest) {
        SMSNotificationRequest request = (SMSNotificationRequest) notificationRequest;
        try {
            validateRequest(request);
            log.info("Sending SMS using clientCode {} with subject {} and data {} by user {}", request.getClientCode(), request.getSubject(), request.getData(), request.getUser());

            return mercuryClient.sendSMS(request);
        } catch (Exception e) {
            return NotificationResponse.getErrorStatus(e.getLocalizedMessage());
        }
    }

    /**
     * checks if the values required for the sms notification is present.
     *
     * @param request data required for the sms notification
     */
    private void validateRequest(SMSNotificationRequest request) {
        String name = Strings.SMS_NOTIFICATION_REQUEST;
        if (Objects.isNull(request.getData())) {
            throw new IllegalArgumentException(String.format("%s: data is null", name));
        }
        if (ObjectUtils.isEmpty(request.getClientCode())) {
            throw new IllegalArgumentException(String.format("%s: clientCode is null or empty", name));
        }
        if (Objects.isNull(request.getSubject())) {
            throw new IllegalArgumentException(String.format("%s: subject is null", name));
        }
        if (Objects.isNull(request.getData().getMessage())) {
            throw new IllegalArgumentException(String.format("%s: message is null", name));
        }
        if (Objects.isNull(request.getData().getTo()) || request.getData().getTo().isBlank()) {
            throw new IllegalArgumentException(String.format("%s: to is null or empty", name));
        }
        if (Objects.isNull(request.getData().getType())) {
            throw new IllegalArgumentException(String.format("%s: type is null", name));
        }
    }
}
