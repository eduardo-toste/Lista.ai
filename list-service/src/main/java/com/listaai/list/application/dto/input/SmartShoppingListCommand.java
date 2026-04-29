package com.listaai.list.application.dto.input;

import java.util.List;

public record SmartShoppingListCommand(

        String name,
        List<ShoppingListParticipantCommand> participants,
        String recipeMessage

) {
}
