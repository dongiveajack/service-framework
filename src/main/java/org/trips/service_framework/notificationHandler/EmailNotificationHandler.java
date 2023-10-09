package org.trips.service_framework.notificationHandler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.trips.service_framework.clients.MercuryClient;
import org.trips.service_framework.clients.request.NotificationRequest;
import org.trips.service_framework.clients.response.NotificationResponse;
import org.trips.service_framework.dtos.EmailNotificationRequest;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationHandler implements NotificationHandler {
    private final MercuryClient mercuryClient;

    /**
     * calls the mercury service along with the request data.
     * returns error status if any exception.
     * @param notificationRequest data required for the email notification
     * @return notification response from mercury service
     * @param <T> base class for the notification request
     */
    @Override
    public <T extends NotificationRequest> NotificationResponse send(@NonNull T notificationRequest) {
        EmailNotificationRequest request = (EmailNotificationRequest) notificationRequest;

        try {
            validateRequest(request);
            log.info("Sending Email using clientCode {} with subject {} and Data {} by user {}", request.getClientCode(), request.getSubject(), request.getData(), request.getUser());

            return mercuryClient.sendEmail(request);
        } catch (Exception e) {
            return NotificationResponse.getErrorStatus(e.getMessage());
        }
    }

    /**
     * checks if the values required for the email notification is present.
     * @param request data required for the email notification
     */

    private void validateRequest(EmailNotificationRequest request) {
        String name = Strings.EMAIL_NOTIFICATION_REQUEST;
        if (Objects.isNull(request.getData())) {
            throw new IllegalArgumentException(String.format("%s: data is null", name));
        }
        if (ObjectUtils.isEmpty(request.getClientCode())) {
            throw new IllegalArgumentException(String.format("%s: clientCode is empty", name));
        }
        if (Objects.isNull(request.getSubject())) {
            throw new IllegalArgumentException(String.format("%s: notification subject is null", name));
        }
        if (Objects.isNull(request.getData())) {
            throw new IllegalArgumentException(String.format("%s: data is null", name));
        }
        if (Objects.isNull(request.getData().getSender()) || request.getData().getSender().isBlank()) {
            throw new IllegalArgumentException(String.format("%s: sender is null or blank", name));
        }
        if (Objects.isNull(request.getData().getCharset())) {
            throw new IllegalArgumentException(String.format("%s: charset is null", name));
        }
        if (ObjectUtils.isEmpty(request.getData().getToAddresses())) {
            throw new IllegalArgumentException(String.format("%s: to addresses is empty", name));
        }
    }
}
