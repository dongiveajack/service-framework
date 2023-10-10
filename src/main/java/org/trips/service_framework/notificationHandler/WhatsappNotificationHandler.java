package org.trips.service_framework.notificationHandler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.trips.service_framework.clients.MercuryClient;
import org.trips.service_framework.clients.request.NotificationRequest;
import org.trips.service_framework.clients.response.NotificationResponse;
import org.trips.service_framework.dtos.WhatsappNotificationRequest;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class WhatsappNotificationHandler implements NotificationHandler {
    private final MercuryClient mercuryClient;

    /**
     * calls the mercury service along with the request data.
     * returns error status if any exception.
     *
     * @param notificationRequest data required for the whatsapp notification
     * @param <T>                 base class for the notification request
     * @return notification response from mercury service
     */
    @Override
    public <T extends NotificationRequest> NotificationResponse send(@NonNull T notificationRequest) {
        WhatsappNotificationRequest request = (WhatsappNotificationRequest) notificationRequest;


        try {
            validateRequest(request);
            log.info("Sending Email using clientCode {} with subject {} and Data {} by user {}", request.getClientCode(), request.getSubject(), request.getData(), request.getUser());

            return mercuryClient.sendWhatsappMessage(request);
        } catch (Exception e) {
            log.error("Error sending whatsapp notification: {}", e.getMessage());

            return NotificationResponse.getErrorStatus(e.getMessage());
        }
    }

    /**
     * checks if the values required for the whatsapp notification is present.
     *
     * @param request data required for the whatsapp notification
     */

    private void validateRequest(WhatsappNotificationRequest request) {
        if (Objects.isNull(request.getData())) {
            throw new IllegalArgumentException(String.format("%s: data is null", Strings.WHATSAPP_NOTIFICATION_REQUEST));
        }
        if (ObjectUtils.isEmpty(request.getClientCode())) {
            throw new IllegalArgumentException(String.format("%s: clientCode is empty", Strings.WHATSAPP_NOTIFICATION_REQUEST));
        }
        if (Objects.isNull(request.getSubject())) {
            throw new IllegalArgumentException(String.format("%s: notification subject is null", Strings.WHATSAPP_NOTIFICATION_REQUEST));
        }
        if (Objects.isNull(request.getData().getData())) {
            throw new IllegalArgumentException(String.format("%s: message data is null", Strings.WHATSAPP_NOTIFICATION_REQUEST));
        }
        if (ObjectUtils.isEmpty(request.getData().getTo())) {
            throw new IllegalArgumentException(String.format("%s: phone numbers is empty", Strings.WHATSAPP_NOTIFICATION_REQUEST));
        }
        if (ObjectUtils.isEmpty(request.getData().getProvider())) {
            throw new IllegalArgumentException(String.format("%s: provider is empty", Strings.WHATSAPP_NOTIFICATION_REQUEST));
        }
        if (Objects.isNull(request.getData().getData().getMessageTemplate())) {
            throw new IllegalArgumentException(String.format("%s: message template is null", Strings.WHATSAPP_NOTIFICATION_REQUEST));
        }
        if (ObjectUtils.isEmpty(request.getData().getData().getMessageTemplate().getTemplateName())) {
            throw new IllegalArgumentException(String.format("%s: template name is empty", Strings.WHATSAPP_NOTIFICATION_REQUEST));
        }
        if (Objects.isNull(request.getData().getData().getMessageTemplate().getLanguage())) {
            throw new IllegalArgumentException(String.format("%s: language is null", Strings.WHATSAPP_NOTIFICATION_REQUEST));
        }
        if (ObjectUtils.isEmpty(request.getData().getData().getMessageTemplate().getLanguage().getCode())) {
            throw new IllegalArgumentException(String.format("%s: language code is empty", Strings.WHATSAPP_NOTIFICATION_REQUEST));
        }
    }
}
