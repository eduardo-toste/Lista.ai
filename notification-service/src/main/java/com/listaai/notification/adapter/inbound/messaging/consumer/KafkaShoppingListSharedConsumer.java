package com.listaai.notification.adapter.inbound.messaging.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.listaai.notification.adapter.inbound.messaging.mapper.ShoppingListSharedEventMapper;
import com.listaai.notification.adapter.inbound.messaging.payload.ShoppingListSharedEvent;
import com.listaai.notification.application.port.inbound.HandleShoppingListSharedUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaShoppingListSharedConsumer {

    private final ObjectMapper objectMapper;
    private final ShoppingListSharedEventMapper shoppingListSharedEventMapper;
    private final HandleShoppingListSharedUseCase handleShoppingListSharedUseCase;

    public KafkaShoppingListSharedConsumer(
            ObjectMapper objectMapper,
            ShoppingListSharedEventMapper shoppingListSharedEventMapper,
            HandleShoppingListSharedUseCase handleShoppingListSharedUseCase
    ) {
        this.objectMapper = objectMapper;
        this.shoppingListSharedEventMapper = shoppingListSharedEventMapper;
        this.handleShoppingListSharedUseCase = handleShoppingListSharedUseCase;
    }

    @KafkaListener(
            topics = "${app.kafka.topic.shopping-list-shared}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String message) {
        ShoppingListSharedEvent event = readEvent(message);
        handleShoppingListSharedUseCase.handle(shoppingListSharedEventMapper.toCommand(event));
    }

    private ShoppingListSharedEvent readEvent(String message) {
        try {
            return objectMapper.readValue(message, ShoppingListSharedEvent.class);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Failed to deserialize shopping list shared event", ex);
        }
    }
}
