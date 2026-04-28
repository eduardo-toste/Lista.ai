package com.listaai.notification.adapter.outbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.listaai.notification.application.dto.output.NotificationRequest;
import com.listaai.notification.application.port.outbound.NotificationSenderPort;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TwilioWhatsAppNotificationSender implements NotificationSenderPort {

    private final ObjectMapper objectMapper;

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String from;

    @Value("${app.twilio.templates.shopping-list-shared}")
    private String shoppingListSharedContentSid;

    @Override
    public void send(NotificationRequest notificationRequest) {
        Twilio.init(accountSid, authToken);

        String contentSid = resolveContentSid(notificationRequest.templateName());
        String contentVariables = toJson(notificationRequest.variables());

        Message.creator(
                        new PhoneNumber("whatsapp:+55" + notificationRequest.recipient()),
                        new PhoneNumber(from),
                        ""
                )
                .setContentSid(contentSid)
                .setContentVariables(contentVariables)
                .create();
    }

    private String resolveContentSid(String templateName) {
        if ("shopping-list-shared".equals(templateName)) {
            return shoppingListSharedContentSid;
        }
        throw new IllegalArgumentException("Unknown template: " + templateName);
    }

    private String toJson(Map<String, String> variables) {
        try {
            return objectMapper.writeValueAsString(variables);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Failed to serialize Twilio content variables", ex);
        }
    }

}
