package com.listaai.list.adapter.inbound.web.request;

import jakarta.validation.constraints.NotBlank;

public record CreateShoppingListParticipantRequest(

        @NotBlank String name,
        @NotBlank String phoneNumber

) {
}
