package org.trips.service_framework.notificationHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.trips.service_framework.clients.MercuryClient;
import org.trips.service_framework.clients.request.NotificationRequest;
import org.trips.service_framework.clients.response.NotificationResponse;
import org.trips.service_framework.dtos.EmailData;
import org.trips.service_framework.utils.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationHandler implements NotificationHandler {
    private final MercuryClient mercuryClient;

    @Override
    public <T> NotificationResponse send(String subject, String clientCode, T data) {
        NotificationRequest<EmailData> request = NotificationRequest.<EmailData>builder()
                .user(Context.getUserId())
                .subject(subject)
                .clientCode(clientCode)
                .data((EmailData) data)
                .build();
        NotificationResponse response = mercuryClient.sendEmail(request);
        log.info("Email Status: {}", response);
        return response;
    }
}
