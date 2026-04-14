package com.listaai.list.adapter.inbound.web.mapper;

import com.listaai.list.adapter.inbound.web.request.CreateShoppingListItemRequest;
import com.listaai.list.adapter.inbound.web.response.ShoppingListItemResponse;
import com.listaai.list.application.dto.input.ShoppingListItemCommand;
import com.listaai.list.application.dto.output.ShoppingListItemOutput;
import org.springframework.stereotype.Component;

@Component
public class ShoppingListItemWebMapper {

    public ShoppingListItemCommand toCommand(CreateShoppingListItemRequest request) {
        return new ShoppingListItemCommand(
                request.name(),
                request.quantity(),
                request.unit()
        );
    }

    public ShoppingListItemResponse toResponse(ShoppingListItemOutput output) {
        return new ShoppingListItemResponse(
                output.id(),
                output.name(),
                output.quantity(),
                output.unit(),
                output.purchased()
        );
    }

}
