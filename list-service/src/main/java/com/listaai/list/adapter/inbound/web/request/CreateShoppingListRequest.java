package com.listaai.list.adapter.inbound.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateShoppingListRequest(

        @NotBlank String name,
        @NotEmpty List<CreateShoppingListItemRequest> items,
        List<CreateShoppingListParticipantRequest> participants

) {
}
