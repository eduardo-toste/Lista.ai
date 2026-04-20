package com.listaai.list.adapter.inbound.web.mapper;

import com.listaai.list.adapter.inbound.web.request.CreateShoppingListParticipantRequest;
import com.listaai.list.adapter.inbound.web.request.UpdateShoppingListParticipantRequest;
import com.listaai.list.adapter.inbound.web.response.ShoppingListParticipantResponse;
import com.listaai.list.application.dto.input.ShoppingListParticipantCommand;
import com.listaai.list.application.dto.output.ShoppingListParticipantOutput;
import org.springframework.stereotype.Component;

@Component
public class ShoppingListParticipantWebMapper {

    public ShoppingListParticipantCommand toCommand(CreateShoppingListParticipantRequest request) {
        return new ShoppingListParticipantCommand(
                request.name(),
                request.phoneNumber()
        );
    }

    public ShoppingListParticipantCommand toCommand(UpdateShoppingListParticipantRequest request) {
        return new ShoppingListParticipantCommand(
                request.name(),
                request.phoneNumber()
        );
    }

    public ShoppingListParticipantResponse toResponse(ShoppingListParticipantOutput output) {
        return new ShoppingListParticipantResponse(
                output.id(),
                output.name(),
                output.phoneNumber()
        );
    }

}
