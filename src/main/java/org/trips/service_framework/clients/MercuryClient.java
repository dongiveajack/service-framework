package org.trips.service_framework.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.trips.service_framework.clients.request.NotificationRequest;
import org.trips.service_framework.clients.response.NotificationResponse;
import org.trips.service_framework.dtos.EmailData;
import org.trips.service_framework.dtos.SMSData;
import org.trips.service_framework.dtos.WhatsappData;

@Component
@FeignClient(name = "mercury", url = "${mercury.base-url}", configuration = MercuryClientInterceptor.class)
public interface MercuryClient {
    @RequestMapping(method = RequestMethod.POST, value = "/notify/sms")
    NotificationResponse sendSMS(@RequestBody NotificationRequest<SMSData> requestBody);

    @RequestMapping(method = RequestMethod.POST, value = "/notify/email")
    NotificationResponse sendEmail(@RequestBody NotificationRequest<EmailData> requestBody);

    @RequestMapping(method = RequestMethod.POST, value = "/notify/whatsapp")
    NotificationResponse sendWhatsappMessage(@RequestBody NotificationRequest<WhatsappData> requestBody);
}
