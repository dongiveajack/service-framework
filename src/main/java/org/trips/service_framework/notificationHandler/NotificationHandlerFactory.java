package org.trips.service_framework.notificationHandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class NotificationHandlerFactory {
    private final SMSNotificationHandler smsNotificationHandler;
    private final EmailNotificationHandler emailNotificationHandler;
    private final WhatsappNotificationHandler whatsappNotificationHandler;

    /**
     * returns the required notification handler based on the type provided.
     *
     * @param type type of notification required
     * @return required notification handler
     */
    public NotificationHandler getNotificationHandler(NotificationType type) {
        switch (type) {
            case SMS:
                return smsNotificationHandler;
            case EMAIL:
                return emailNotificationHandler;
            case WHATSAPP:
                return whatsappNotificationHandler;
            default:
                throw new IllegalArgumentException(String.format("Invalid notification type: %s", type));
        }
    }
}
