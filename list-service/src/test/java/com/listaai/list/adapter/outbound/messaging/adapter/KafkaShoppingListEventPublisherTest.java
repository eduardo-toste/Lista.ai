package com.listaai.list.adapter.outbound.messaging.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.listaai.list.adapter.outbound.messaging.exception.ShoppingListEventPublishException;
import com.listaai.list.adapter.outbound.messaging.payload.ShoppingListSharedEvent;
import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.model.ShoppingList;
import com.listaai.list.domain.model.ShoppingListItem;
import com.listaai.list.domain.model.ShoppingListParticipant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaShoppingListEventPublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaShoppingListEventPublisher publisher;

    @Test
    void shouldSerializeAndSendShoppingListSharedEvent() throws Exception {
        ShoppingList shoppingList = new ShoppingList(
                1L,
                "Churrasco",
                List.of(new ShoppingListItem(1L, "Carvao", 2, ItemUnit.UN, false)),
                List.of(new ShoppingListParticipant(1L, "Eduardo", "11999999999"))
        );
        ReflectionTestUtils.setField(publisher, "shoppingListSharedTopic", "shopping-list.shared");
        when(objectMapper.writeValueAsString(org.mockito.ArgumentMatchers.any(ShoppingListSharedEvent.class)))
                .thenReturn("{\"eventType\":\"shopping-list.shared\"}");

        publisher.publishShoppingListShared(shoppingList);

        ArgumentCaptor<ShoppingListSharedEvent> eventCaptor = ArgumentCaptor.forClass(ShoppingListSharedEvent.class);
        verify(objectMapper).writeValueAsString(eventCaptor.capture());
        assertEquals("shopping-list.shared", eventCaptor.getValue().eventType());
        assertEquals(1L, eventCaptor.getValue().shoppingListId());
        verify(kafkaTemplate).send("shopping-list.shared", "1", "{\"eventType\":\"shopping-list.shared\"}");
    }

    @Test
    void shouldWrapJsonProcessingException() throws Exception {
        ShoppingList shoppingList = new ShoppingList(
                1L,
                "Churrasco",
                List.of(new ShoppingListItem(1L, "Carvao", 2, ItemUnit.UN, false)),
                List.of(new ShoppingListParticipant(1L, "Eduardo", "11999999999"))
        );
        ReflectionTestUtils.setField(publisher, "shoppingListSharedTopic", "shopping-list.shared");
        when(objectMapper.writeValueAsString(org.mockito.ArgumentMatchers.any(ShoppingListSharedEvent.class)))
                .thenThrow(new JsonProcessingException("boom") {});

        ShoppingListEventPublishException exception = assertThrows(
                ShoppingListEventPublishException.class,
                () -> publisher.publishShoppingListShared(shoppingList)
        );

        assertThat(exception.getCause()).isInstanceOf(JsonProcessingException.class);
        assertEquals("Failed to serialize shopping list event", exception.getMessage());
    }
}
