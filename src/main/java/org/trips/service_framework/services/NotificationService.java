package org.trips.service_framework.services;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationHandlerFactory notificationHandlerFactory;
    public NotificationResponse sendSMSNotification(String clientCode, SMSDto smsDto) {
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

    public NotificationResponse sendEmailNotification(String clientCode, EmailDto emailDto) {
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

    public NotificationResponse sendWhatsappNotification(String clientCode, WhatsappDto whatsappDto) {
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
