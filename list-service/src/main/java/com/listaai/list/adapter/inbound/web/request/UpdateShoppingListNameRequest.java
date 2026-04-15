package com.listaai.list.adapter.inbound.web.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateShoppingListNameRequest(

        @NotBlank(message = "Shopping list name must not be blank.")
        String name

) {
}
