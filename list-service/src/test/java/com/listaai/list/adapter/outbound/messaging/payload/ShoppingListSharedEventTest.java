package com.listaai.list.adapter.outbound.messaging.payload;

import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.model.ShoppingList;
import com.listaai.list.domain.model.ShoppingListItem;
import com.listaai.list.domain.model.ShoppingListParticipant;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShoppingListSharedEventTest {

    @Test
    void shouldCreateEventFromDomain() {
        ShoppingList shoppingList = new ShoppingList(
                1L,
                "Churrasco",
                List.of(new ShoppingListItem(10L, "Carvao", 2, ItemUnit.UN, false)),
                List.of(new ShoppingListParticipant(20L, "Eduardo", "11999999999"))
        );

        ShoppingListSharedEvent event = ShoppingListSharedEvent.fromDomain(shoppingList);

        assertThat(event.eventType()).isEqualTo("shopping-list.shared");
        assertThat(Instant.parse(event.occurredAt())).isNotNull();
        assertThat(event.shoppingListId()).isEqualTo(1L);
        assertThat(event.shoppingListName()).isEqualTo("Churrasco");
        assertThat(event.items()).singleElement().satisfies(item -> {
            assertThat(item.id()).isEqualTo(10L);
            assertThat(item.name()).isEqualTo("Carvao");
            assertThat(item.quantity()).isEqualTo(2);
            assertThat(item.unit()).isEqualTo(ItemUnit.UN);
            assertThat(item.purchased()).isFalse();
        });
        assertThat(event.participants()).singleElement().satisfies(participant -> {
            assertThat(participant.id()).isEqualTo(20L);
            assertThat(participant.name()).isEqualTo("Eduardo");
            assertThat(participant.phoneNumber()).isEqualTo("11999999999");
        });
    }
}
