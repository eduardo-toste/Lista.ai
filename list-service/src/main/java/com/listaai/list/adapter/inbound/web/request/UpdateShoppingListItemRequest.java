package com.listaai.list.adapter.inbound.web.request;

import com.listaai.list.domain.enums.ItemUnit;
import jakarta.validation.constraints.Min;

public record UpdateShoppingListItemRequest(

        String name,
        @Min(value = 1, message = "Quantity must not be less than 1") int quantity,
        ItemUnit unit

) {
}
