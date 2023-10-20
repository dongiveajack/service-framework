package org.trips.service_framework.notificationHandler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.trips.service_framework.clients.MercuryClient;
import org.trips.service_framework.clients.request.NotificationRequest;
import org.trips.service_framework.clients.response.NotificationResponse;
import org.trips.service_framework.dtos.EmailNotificationRequest;

import java.util.Objects;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "mercury", name = "base-url")
@RequiredArgsConstructor
public class EmailNotificationHandler implements NotificationHandler {
    private final MercuryClient mercuryClient;

    /**
     * calls the mercury service along with the request data.
     * returns error status if any exception.
     *
     * @param notificationRequest data required for the email notification
     * @param <T>                 base class for the notification request
     * @return notification response from mercury service
     */
    @Override
    public <T extends NotificationRequest> NotificationResponse send(@NonNull T notificationRequest) {
        EmailNotificationRequest request = (EmailNotificationRequest) notificationRequest;

        try {
            validateRequest(request);
            log.info("Sending Email using clientCode {} with subject {} and Data {} by user {}", request.getClientCode(), request.getSubject(), request.getData(), request.getUser());

            return mercuryClient.sendEmail(request);
        } catch (Exception e) {
            log.error("Error sending email notification: {}", e.getMessage());

            return NotificationResponse.getErrorStatus(e.getMessage());
        }
    }

    /**
     * checks if the values required for the email notification is present.
     *
     * @param request data required for the email notification
     */

    private void validateRequest(EmailNotificationRequest request) {
        if (Objects.isNull(request.getData())) {
            throw new IllegalArgumentException(String.format("%s: data is null", Strings.EMAIL_NOTIFICATION_REQUEST));
        }
        if (ObjectUtils.isEmpty(request.getClientCode())) {
            throw new IllegalArgumentException(String.format("%s: clientCode is empty", Strings.EMAIL_NOTIFICATION_REQUEST));
        }
        if (Objects.isNull(request.getSubject())) {
            throw new IllegalArgumentException(String.format("%s: notification subject is null", Strings.EMAIL_NOTIFICATION_REQUEST));
        }
        if (Objects.isNull(request.getData())) {
            throw new IllegalArgumentException(String.format("%s: data is null", Strings.EMAIL_NOTIFICATION_REQUEST));
        }
        if (Objects.isNull(request.getData().getSender()) || request.getData().getSender().isBlank()) {
            throw new IllegalArgumentException(String.format("%s: sender is null or blank", Strings.EMAIL_NOTIFICATION_REQUEST));
        }
        if (Objects.isNull(request.getData().getCharset())) {
            throw new IllegalArgumentException(String.format("%s: charset is null", Strings.EMAIL_NOTIFICATION_REQUEST));
        }
        if (ObjectUtils.isEmpty(request.getData().getToAddresses())) {
            throw new IllegalArgumentException(String.format("%s: to addresses is empty", Strings.EMAIL_NOTIFICATION_REQUEST));
        }
    }
}
