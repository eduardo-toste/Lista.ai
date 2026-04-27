package com.listaai.list.adapter.outbound.messaging.payload;

import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.model.ShoppingList;

import java.time.Instant;
import java.util.List;

public record ShoppingListSharedEvent(

        String eventType,
        String occurredAt,
        Long shoppingListId,
        String shoppingListName,
        List<ShoppingListSharedItemEvent> items,
        List<ShoppingListSharedParticipantEvent> participants

) {

    public static ShoppingListSharedEvent fromDomain(ShoppingList shoppingList) {
        return new ShoppingListSharedEvent(
                "shopping-list.shared",
                Instant.now().toString(),
                shoppingList.getId(),
                shoppingList.getName(),
                shoppingList.getItems().stream()
                        .map(item -> new ShoppingListSharedItemEvent(
                                item.getId(),
                                item.getName(),
                                item.getQuantity(),
                                item.getUnit(),
                                item.isPurchased()
                        ))
                        .toList(),
                shoppingList.getParticipants().stream()
                        .map(participant -> new ShoppingListSharedParticipantEvent(
                                participant.getId(),
                                participant.getName(),
                                participant.getPhoneNumber()
                        ))
                        .toList()
        );
    }

    public record ShoppingListSharedItemEvent(
            Long id,
            String name,
            int quantity,
            ItemUnit unit,
            boolean purchased
    ) {
    }

    public record ShoppingListSharedParticipantEvent(
            Long id,
            String name,
            String phoneNumber
    ) {
    }
}
