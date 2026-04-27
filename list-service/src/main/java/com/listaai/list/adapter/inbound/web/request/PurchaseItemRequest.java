package com.listaai.list.adapter.inbound.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PurchaseItemRequest(

        @NotNull(message = "Purchase status must not be null")
        Boolean purchased

) {
}
