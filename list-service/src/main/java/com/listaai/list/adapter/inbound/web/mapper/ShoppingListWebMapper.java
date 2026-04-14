package com.listaai.list.adapter.inbound.web.mapper;

import com.listaai.list.adapter.inbound.web.request.CreateShoppingListRequest;
import com.listaai.list.adapter.inbound.web.response.ShoppingListResponse;
import com.listaai.list.application.dto.input.CreateShoppingListCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import org.springframework.stereotype.Component;

@Component
public class ShoppingListWebMapper {

    private final ShoppingListItemWebMapper shoppingListItemWebMapper;
    private final ShoppingListParticipantWebMapper shoppingListParticipantWebMapper;

    public ShoppingListWebMapper(ShoppingListItemWebMapper shoppingListItemWebMapper, ShoppingListParticipantWebMapper shoppingListParticipantWebMapper) {
        this.shoppingListItemWebMapper = shoppingListItemWebMapper;
        this.shoppingListParticipantWebMapper = shoppingListParticipantWebMapper;
    }

    public CreateShoppingListCommand toCommand(CreateShoppingListRequest request) {
        return new CreateShoppingListCommand(
                request.name(),
                request.items().stream()
                        .map(shoppingListItemWebMapper::toCommand)
                        .toList(),
                request.participants().stream()
                        .map(shoppingListParticipantWebMapper::toCommand)
                        .toList()
        );
    }

    public ShoppingListResponse toResponse(ShoppingListOutput output) {
        return new ShoppingListResponse(
                output.id(),
                output.name(),
                output.items().stream()
                        .map(shoppingListItemWebMapper::toResponse)
                        .toList(),
                output.participants().stream()
                        .map(shoppingListParticipantWebMapper::toResponse)
                        .toList()
        );
    }

}
