package com.listaai.list.adapter.inbound.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateShoppingListParticipantRequest(

        @NotBlank(message = "Participant name must not be blank")
        String name,

        @NotBlank(message = "Participant phone number must not be blank")
        @Pattern(regexp = "^\\d{10,11}$", message = "Phone number must contain only digits and have 10 or 11 characters")
        String phoneNumber

) {
}
