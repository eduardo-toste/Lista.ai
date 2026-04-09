package com.listaai.list.application.dto.input;

import com.listaai.list.domain.enums.ItemUnit;

public record ShoppingListItemCommand(

        String name,
        int quantity,
        ItemUnit unit

) {
}
