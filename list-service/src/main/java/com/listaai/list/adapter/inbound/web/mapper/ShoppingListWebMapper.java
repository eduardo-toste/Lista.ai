package com.listaai.list.adapter.inbound.web.mapper;

import com.listaai.list.adapter.inbound.web.request.CreateShoppingListRequest;
import com.listaai.list.adapter.inbound.web.request.CreateSmartShoppingListRequest;
import com.listaai.list.adapter.inbound.web.response.ShoppingListResponse;
import com.listaai.list.application.dto.input.ShoppingListCommand;
import com.listaai.list.application.dto.input.SmartShoppingListCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ShoppingListWebMapper {

    private final ShoppingListItemWebMapper shoppingListItemWebMapper;
    private final ShoppingListParticipantWebMapper shoppingListParticipantWebMapper;

    public ShoppingListWebMapper(ShoppingListItemWebMapper shoppingListItemWebMapper, ShoppingListParticipantWebMapper shoppingListParticipantWebMapper) {
        this.shoppingListItemWebMapper = shoppingListItemWebMapper;
        this.shoppingListParticipantWebMapper = shoppingListParticipantWebMapper;
    }

    public ShoppingListCommand toCommand(CreateShoppingListRequest request) {
        return new ShoppingListCommand(
                request.name(),
                request.items().stream()
                        .map(shoppingListItemWebMapper::toCommand)
                        .toList(),
                request.participants().stream()
                        .map(shoppingListParticipantWebMapper::toCommand)
                        .toList()
        );
    }

    public SmartShoppingListCommand toCommand(CreateSmartShoppingListRequest request) {
        return new SmartShoppingListCommand(
                request.name(),
                request.participants().stream()
                        .map(shoppingListParticipantWebMapper::toCommand)
                        .toList(),
                request.recipeMessage()
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

    public Page<ShoppingListResponse> toPageResponse(Page<ShoppingListOutput> pageOutput) {
        return pageOutput.map(this::toResponse);
    }

}
