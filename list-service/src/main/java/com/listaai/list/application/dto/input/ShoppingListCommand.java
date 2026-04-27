package com.listaai.list.application.dto.input;

import java.util.List;

public record ShoppingListCommand(

        String name,
        List<ShoppingListItemCommand> items,
        List<ShoppingListParticipantCommand> participants

) {
}
