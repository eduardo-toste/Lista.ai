package com.listaai.list.adapter.inbound.web.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateSmartShoppingListRequest(

        @NotBlank(message = "Shopping list name must not be blank")
        String name,

        @NotNull
        @Valid
        List<CreateShoppingListParticipantRequest> participants,

        @NotBlank(message = "Recipe message must not be blank")
        String recipeMessage

) {
}
