package com.listaai.notification.adapter.outbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.listaai.notification.application.dto.output.NotificationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TwilioWhatsAppNotificationSenderTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private TwilioWhatsAppNotificationSender sender;

    @Test
    void shouldThrowWhenTemplateNameIsUnknown() {
        ReflectionTestUtils.setField(sender, "accountSid", "sid");
        ReflectionTestUtils.setField(sender, "authToken", "token");
        ReflectionTestUtils.setField(sender, "from", "whatsapp:+5511999999999");
        ReflectionTestUtils.setField(sender, "shoppingListSharedContentSid", "HX123");

        NotificationRequest request = new NotificationRequest(
                "WHATSAPP",
                "11999999999",
                "unknown-template",
                Map.of("1", "Eduardo")
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> sender.send(request)
        );

        assertEquals("Unknown template: unknown-template", exception.getMessage());
    }

    @Test
    void shouldThrowWhenVariablesCannotBeSerialized() throws Exception {
        ReflectionTestUtils.setField(sender, "accountSid", "sid");
        ReflectionTestUtils.setField(sender, "authToken", "token");
        ReflectionTestUtils.setField(sender, "from", "whatsapp:+5511999999999");
        ReflectionTestUtils.setField(sender, "shoppingListSharedContentSid", "HX123");

        NotificationRequest request = new NotificationRequest(
                "WHATSAPP",
                "11999999999",
                "shopping-list-shared",
                Map.of("1", "Eduardo")
        );
        when(objectMapper.writeValueAsString(request.variables()))
                .thenThrow(new JsonProcessingException("boom") {});

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> sender.send(request)
        );

        assertEquals("Failed to serialize Twilio content variables", exception.getMessage());
        assertInstanceOf(JsonProcessingException.class, exception.getCause());
    }
}
