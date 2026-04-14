package com.listaai.list.adapter.inbound.web.request;

import com.listaai.list.domain.enums.ItemUnit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateShoppingListItemRequest(

        @NotBlank(message = "Item name must not be blank")
        String name,

        @Min(value = 1, message = "Item quantity must be at least 1")
        @NotNull(message = "Item quantity must not be null")
        int quantity,

        @NotNull(message = "Item unit must not be null")
        ItemUnit unit

) {
}
