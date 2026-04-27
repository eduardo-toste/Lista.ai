package com.listaai.list.adapter.outbound.messaging.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.listaai.list.adapter.outbound.messaging.exception.ShoppingListEventPublishException;
import com.listaai.list.adapter.outbound.messaging.payload.ShoppingListSharedEvent;
import com.listaai.list.application.port.outbound.ShoppingListEventPublisherPort;
import com.listaai.list.domain.model.ShoppingList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaShoppingListEventPublisher implements ShoppingListEventPublisherPort {

    @Value("${app.kafka.topic.shopping-list-shared}")
    private String shoppingListSharedTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaShoppingListEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishShoppingListShared(ShoppingList shoppingList) {
        try {
            ShoppingListSharedEvent event = ShoppingListSharedEvent.fromDomain(shoppingList);

            kafkaTemplate.send(
                    shoppingListSharedTopic,
                    shoppingList.getId().toString(),
                    objectMapper.writeValueAsString(event)
            );
        } catch (JsonProcessingException ex) {
            throw new ShoppingListEventPublishException("Failed to serialize shopping list event", ex);
        }
    }
}
