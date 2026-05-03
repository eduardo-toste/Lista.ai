package com.listaai.notification.adapter.inbound.messaging.mapper;

import com.listaai.notification.adapter.inbound.messaging.payload.ShoppingListSharedEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShoppingListSharedEventMapperTest {

    private final ShoppingListSharedEventMapper mapper = new ShoppingListSharedEventMapper();

    @Test
    void shouldMapEventToCommand() {
        ShoppingListSharedEvent event = new ShoppingListSharedEvent(
                "shopping-list.shared",
                "2026-05-03T12:00:00Z",
                1L,
                "Churrasco",
                List.of(new ShoppingListSharedEvent.ShoppingListSharedItemEvent(10L, "Carvao", 2, "UN", false)),
                List.of(new ShoppingListSharedEvent.ShoppingListSharedParticipantEvent(20L, "Eduardo", "11999999999"))
        );

        var result = mapper.toCommand(event);

        assertThat(result.eventType()).isEqualTo("shopping-list.shared");
        assertThat(result.shoppingListId()).isEqualTo(1L);
        assertThat(result.shoppingListName()).isEqualTo("Churrasco");
        assertThat(result.items()).singleElement().satisfies(item -> {
            assertThat(item.id()).isEqualTo(10L);
            assertThat(item.name()).isEqualTo("Carvao");
            assertThat(item.quantity()).isEqualTo(2);
            assertThat(item.unit()).isEqualTo("UN");
            assertThat(item.purchased()).isFalse();
        });
        assertThat(result.participants()).singleElement().satisfies(participant -> {
            assertThat(participant.id()).isEqualTo(20L);
            assertThat(participant.name()).isEqualTo("Eduardo");
            assertThat(participant.phoneNumber()).isEqualTo("11999999999");
        });
    }
}
