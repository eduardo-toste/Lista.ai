package com.listaai.list.adapter.inbound.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateShoppingListParticipantRequest(

        @NotBlank
        String name,

        @NotBlank
        @Pattern(regexp = "^\\d{10,11}$", message = "Phone number must contain only digits and have 10 or 11 characters")
        String phoneNumber

) {
}
