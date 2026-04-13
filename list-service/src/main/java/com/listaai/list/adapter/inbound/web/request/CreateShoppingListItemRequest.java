package com.listaai.list.adapter.inbound.web.request;

import com.listaai.list.domain.enums.ItemUnit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateShoppingListItemRequest(

        @NotBlank String name,
        @Min(1) @NotNull int quantity,
        @NotNull ItemUnit unit

) {
}
