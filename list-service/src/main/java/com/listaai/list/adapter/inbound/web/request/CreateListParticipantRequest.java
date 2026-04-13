package com.listaai.list.adapter.inbound.web.request;

import jakarta.validation.constraints.NotBlank;

public record CreateListParticipantRequest(

        @NotBlank String name,
        @NotBlank String number

) {
}
