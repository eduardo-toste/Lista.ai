package com.listaai.notification.adapter.inbound.messaging.mapper;

import com.listaai.notification.adapter.inbound.messaging.payload.ShoppingListSharedEvent;
import com.listaai.notification.application.dto.input.HandleShoppingListSharedCommand;
import org.springframework.stereotype.Component;

@Component
public class ShoppingListSharedEventMapper {

    public HandleShoppingListSharedCommand toCommand(ShoppingListSharedEvent event) {
        return new HandleShoppingListSharedCommand(
                event.eventType(),
                event.occurredAt(),
                event.shoppingListId(),
                event.shoppingListName(),
                event.items().stream()
                        .map(item -> new HandleShoppingListSharedCommand.SharedItemInput(
                                item.id(),
                                item.name(),
                                item.quantity(),
                                item.unit(),
                                item.purchased()
                        ))
                        .toList(),
                event.participants().stream()
                        .map(participant -> new HandleShoppingListSharedCommand.SharedParticipantInput(
                                participant.id(),
                                participant.name(),
                                participant.phoneNumber()
                        ))
                        .toList()
        );
    }
}
