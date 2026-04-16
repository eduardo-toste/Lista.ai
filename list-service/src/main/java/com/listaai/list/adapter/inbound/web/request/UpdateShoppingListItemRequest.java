package com.listaai.list.adapter.inbound.web.request;

import com.listaai.list.domain.enums.ItemUnit;

public record UpdateShoppingListItemRequest(

        String name,
        int quantity,
        ItemUnit unit

) {
}
