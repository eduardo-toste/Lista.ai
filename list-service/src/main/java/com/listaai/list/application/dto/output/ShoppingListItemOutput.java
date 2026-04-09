package com.listaai.list.application.dto.output;

import com.listaai.list.domain.enums.ItemUnit;

public record ShoppingListItemOutput(

        Long id,
        String name,
        int quantity,
        ItemUnit unit,
        boolean purchased

) {
}
