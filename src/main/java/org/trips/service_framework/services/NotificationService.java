package org.trips.service_framework.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.trips.service_framework.clients.response.NotificationResponse;
import org.trips.service_framework.dtos.EmailNotificationRequest;
import org.trips.service_framework.dtos.SMSNotificationRequest;
import org.trips.service_framework.dtos.WhatsappNotificationRequest;
import org.trips.service_framework.notificationHandler.NotificationHandler;
import org.trips.service_framework.notificationHandler.NotificationHandlerFactory;
import org.trips.service_framework.notificationHandler.NotificationType;
import org.trips.service_framework.notificationHandler.dtos.EmailDto;
import org.trips.service_framework.notificationHandler.dtos.SMSDto;
import org.trips.service_framework.notificationHandler.dtos.WhatsappDto;
import org.trips.service_framework.utils.Context;

@Component
@ConditionalOnProperty({"service.client-id", "mercury.base-url"})
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationHandlerFactory notificationHandlerFactory;
    @Value("${service.client-id}")
    private String clientCode;

    /**
     * convert the dto given to sms notification request.
     * send the request to sms notification handler.
     *
     * @param smsDto     dto containing the values for notification request
     * @return notification response from handler
     */
    public NotificationResponse sendSMSNotification(SMSDto smsDto) {
        SMSNotificationRequest request = SMSNotificationRequest.builder()
                .data(SMSNotificationRequest.Data.builder()
                        .to(smsDto.getTo())
                        .type(smsDto.getType())
                        .message(smsDto.getMessage())
                        .build())
                .build();
        request.setUser(Context.getUserId());
        request.setSubject(smsDto.getNotificationSubject());
        request.setClientCode(clientCode);

        NotificationHandler notificationHandler = notificationHandlerFactory.getNotificationHandler(NotificationType.SMS);

        return notificationHandler.send(request);
    }

    /**
     * convert the dto given to email notification request.
     * send the request to email notification handler.
     *
     * @param emailDto   dto containing the values for notification request
     * @return notification response from handler
     */

    public NotificationResponse sendEmailNotification(EmailDto emailDto) {
        EmailNotificationRequest request = EmailNotificationRequest.builder()
                .data(EmailNotificationRequest.Data.builder()
                        .sender(emailDto.getSender())
                        .toAddresses(emailDto.getToAddresses())
                        .body(emailDto.getBody())
                        .subject(emailDto.getSubject())
                        .html(emailDto.getHtml())
                        .ccAddresses(emailDto.getCcAddresses())
                        .bccAddresses(emailDto.getBccAddresses())
                        .charset(emailDto.getCharset())
                        .build())
                .build();
        request.setUser(Context.getUserId());
        request.setSubject(emailDto.getNotificationSubject());
        request.setClientCode(clientCode);

        NotificationHandler notificationHandler = notificationHandlerFactory.getNotificationHandler(NotificationType.EMAIL);

        return notificationHandler.send(request);
    }

    /**
     * convert the dto given to whatsapp notification request.
     * send the request to whatsapp notification handler
     *
     * @param whatsappDto dto containing the values for notification request
     * @return notification response from handler
     */

    public NotificationResponse sendWhatsappNotification(WhatsappDto whatsappDto) {
        WhatsappNotificationRequest request = WhatsappNotificationRequest.builder()
                .data(WhatsappNotificationRequest.Data.builder()
                        .provider(whatsappDto.getProvider())
                        .to(WhatsappNotificationRequest.Data.getContacts(whatsappDto.getPhoneNumbers()))
                        .data(WhatsappNotificationRequest.Data.getMessageData(whatsappDto.getTemplateName(), whatsappDto.getLanguageCode(), whatsappDto.getMediaType(), whatsappDto.getMediaUrl(), whatsappDto.getParams()))
                        .build())
                .build();
        request.setUser(Context.getUserId());
        request.setSubject(whatsappDto.getNotificationSubject());
        request.setClientCode(clientCode);
        NotificationHandler notificationHandler = notificationHandlerFactory.getNotificationHandler(NotificationType.WHATSAPP);

        return notificationHandler.send(request);
    }
}
