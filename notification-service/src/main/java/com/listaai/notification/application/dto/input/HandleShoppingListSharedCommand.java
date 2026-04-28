package com.listaai.notification.application.dto.input;

import java.util.List;

public record HandleShoppingListSharedCommand(

        String eventType,
        String occurredAt,
        Long shoppingListId,
        String shoppingListName,
        List<SharedItemInput> items,
        List<SharedParticipantInput> participants

) {

    public record SharedItemInput(
            Long id,
            String name,
            int quantity,
            String unit,
            boolean purchased
    ) {
    }

    public record SharedParticipantInput(
            Long id,
            String name,
            String phoneNumber
    ) {
    }
}
