package com.listaai.notification.application.port.outbound;

import com.listaai.notification.application.dto.output.NotificationRequest;

public interface NotificationSenderPort {

    void send(NotificationRequest notificationRequest);

}
