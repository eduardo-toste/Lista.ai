package com.listaai.notification.adapter.inbound.messaging.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.listaai.notification.adapter.inbound.messaging.mapper.ShoppingListSharedEventMapper;
import com.listaai.notification.adapter.inbound.messaging.payload.ShoppingListSharedEvent;
import com.listaai.notification.application.dto.input.HandleShoppingListSharedCommand;
import com.listaai.notification.application.port.inbound.HandleShoppingListSharedUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaShoppingListSharedConsumerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ShoppingListSharedEventMapper shoppingListSharedEventMapper;

    @Mock
    private HandleShoppingListSharedUseCase handleShoppingListSharedUseCase;

    @InjectMocks
    private KafkaShoppingListSharedConsumer consumer;

    @Test
    void shouldDeserializeEventMapToCommandAndHandle() throws Exception {
        String message = "{\"eventType\":\"shopping-list.shared\"}";
        ShoppingListSharedEvent event = new ShoppingListSharedEvent(
                "shopping-list.shared",
                "2026-05-03T12:00:00Z",
                1L,
                "Churrasco",
                List.of(),
                List.of()
        );
        HandleShoppingListSharedCommand command = new HandleShoppingListSharedCommand(
                event.eventType(),
                event.occurredAt(),
                event.shoppingListId(),
                event.shoppingListName(),
                List.of(),
                List.of()
        );

        when(objectMapper.readValue(message, ShoppingListSharedEvent.class)).thenReturn(event);
        when(shoppingListSharedEventMapper.toCommand(event)).thenReturn(command);

        consumer.consume(message);

        verify(handleShoppingListSharedUseCase).handle(command);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenMessageCannotBeDeserialized() throws Exception {
        when(objectMapper.readValue("invalid", ShoppingListSharedEvent.class))
                .thenThrow(new JsonProcessingException("boom") {});

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> consumer.consume("invalid")
        );

        assertEquals("Failed to deserialize shopping list shared event", exception.getMessage());
    }
}
