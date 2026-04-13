package com.listaai.list.adapter.inbound.web.mapper;

import com.listaai.list.adapter.inbound.web.request.CreateShoppingListParticipantRequest;
import com.listaai.list.adapter.inbound.web.response.ShoppingListItemResponse;
import com.listaai.list.adapter.inbound.web.response.ShoppingListParticipantResponse;
import com.listaai.list.application.dto.input.ShoppingListParticipantCommand;
import com.listaai.list.application.dto.output.ShoppingListItemOutput;
import com.listaai.list.application.dto.output.ShoppingListParticipantOutput;
import com.listaai.list.domain.model.ShoppingListParticipant;
import org.springframework.stereotype.Component;

@Component
public class ShoppingListParticipantMapper {

    public ShoppingListParticipantCommand toCommand(CreateShoppingListParticipantRequest request) {
        return new ShoppingListParticipantCommand(
                request.name(),
                request.number()
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
