package org.trips.service_framework.notificationHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.trips.service_framework.clients.MercuryClient;
import org.trips.service_framework.clients.request.NotificationRequest;
import org.trips.service_framework.clients.response.NotificationResponse;
import org.trips.service_framework.dtos.SMSData;
import org.trips.service_framework.utils.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class SMSNotificationHandler implements NotificationHandler {
    private final MercuryClient mercuryClient;

    @Override
    public <T> NotificationResponse send(String subject, String clientCode, T data) {
        NotificationRequest<SMSData> request = NotificationRequest.<SMSData>builder()
                .user(Context.getUserId())
                .clientCode(clientCode)
                .subject(subject)
                .data((SMSData) data)
                .build();
        NotificationResponse response = mercuryClient.sendSMS(request);
        log.info("SMS status: {}", response);

        return response;
    }
}
