package com.listaai.list.application.dto.output;

import java.util.List;

public record ShoppingListOutput(

        Long id,
        String name,
        List<ShoppingListItemOutput> items,
        List<ShoppingListParticipantOutput> participants

) {
}
