package com.listaai.notification.application.dto.output;

import java.util.Map;

public record NotificationRequest(

        String channel,
        String recipient,
        String templateName,
        Map<String, String> variables

) {
}
