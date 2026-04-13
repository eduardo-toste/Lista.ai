package com.listaai.list.adapter.inbound.web.response;

import com.listaai.list.domain.enums.ItemUnit;

public record ShoppingListItemResponse(

        Long id,
        String name,
        int quantity,
        ItemUnit unit,
        boolean purchased

) {
}
