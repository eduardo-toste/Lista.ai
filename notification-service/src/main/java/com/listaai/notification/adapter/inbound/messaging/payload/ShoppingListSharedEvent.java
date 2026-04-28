package com.listaai.notification.adapter.inbound.messaging.payload;

import java.util.List;

public record ShoppingListSharedEvent(

        String eventType,
        String occurredAt,
        Long shoppingListId,
        String shoppingListName,
        List<ShoppingListSharedItemEvent> items,
        List<ShoppingListSharedParticipantEvent> participants

) {

    public record ShoppingListSharedItemEvent(
            Long id,
            String name,
            int quantity,
            String unit,
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
