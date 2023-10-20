package org.trips.service_framework.clients;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.trips.service_framework.clients.response.NotificationResponse;
import org.trips.service_framework.dtos.EmailNotificationRequest;
import org.trips.service_framework.dtos.SMSNotificationRequest;
import org.trips.service_framework.dtos.WhatsappNotificationRequest;

@Component
@ConditionalOnProperty(prefix = "mercury", name = "base-url")
@FeignClient(name = "mercury", url = "${mercury.base-url}", configuration = MercuryClientInterceptor.class)
public interface MercuryClient {
    @RequestMapping(method = RequestMethod.POST, value = "/notify/sms")
    NotificationResponse sendSMS(@RequestBody SMSNotificationRequest requestBody);

    @RequestMapping(method = RequestMethod.POST, value = "/notify/email")
    NotificationResponse sendEmail(@RequestBody EmailNotificationRequest requestBody);

    @RequestMapping(method = RequestMethod.POST, value = "/notify/whatsapp")
    NotificationResponse sendWhatsappMessage(@RequestBody WhatsappNotificationRequest requestBody);
}
