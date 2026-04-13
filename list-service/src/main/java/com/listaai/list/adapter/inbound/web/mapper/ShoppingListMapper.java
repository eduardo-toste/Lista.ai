package com.listaai.list.adapter.inbound.web.mapper;

import com.listaai.list.adapter.inbound.web.request.CreateShoppingListRequest;
import com.listaai.list.adapter.inbound.web.response.ShoppingListResponse;
import com.listaai.list.application.dto.input.CreateShoppingListCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.domain.model.ShoppingListItem;
import com.listaai.list.domain.model.ShoppingListParticipant;
import org.springframework.stereotype.Component;

@Component
public class ShoppingListMapper {

    private ShoppingListItemMapper shoppingListItemMapper;
    private ShoppingListParticipantMapper shoppingListParticipantMapper;

    public ShoppingListMapper(ShoppingListItemMapper shoppingListItemMapper, ShoppingListParticipantMapper shoppingListParticipantMapper) {
        this.shoppingListItemMapper = shoppingListItemMapper;
        this.shoppingListParticipantMapper = shoppingListParticipantMapper;
    }

    public CreateShoppingListCommand toCommand(CreateShoppingListRequest request) {
        return new CreateShoppingListCommand(
                request.name(),
                request.items().stream()
                        .map(item -> shoppingListItemMapper.toCommand(item))
                        .toList(),
                request.participants().stream()
                        .map(participant -> shoppingListParticipantMapper.toCommand(participant))
                        .toList()
        );
    }

    public ShoppingListResponse toResponse(ShoppingListOutput output) {
        return new ShoppingListResponse(
                output.id(),
                output.name(),
                output.items().stream()
                        .map(item -> shoppingListItemMapper.toResponse(item))
                        .toList(),
                output.participants().stream()
                        .map(participant -> shoppingListParticipantMapper.toResponse(participant))
                        .toList()
        );
    }

}
