package com.listaai.list.application.mapper;

import com.listaai.list.application.dto.input.CreateShoppingListCommand;
import com.listaai.list.application.dto.input.ShoppingListItemCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.domain.model.ShoppingList;
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

    public ShoppingList toDomain(CreateShoppingListCommand command) {
        return new ShoppingList(
                null,
                command.name(),
                command.items().stream()
                        .map(item -> shoppingListItemMapper.toDomain(item))
                        .toList(),
                command.participants().stream()
                        .map(participant -> shoppingListParticipantMapper.toDomain(participant))
                        .toList()
        );
    }

    public ShoppingListOutput toOutput(ShoppingList domain) {
        return new ShoppingListOutput(
                domain.getId(),
                domain.getName(),
                domain.getItems().stream()
                        .map(item -> shoppingListItemMapper.toOutput(item))
                        .toList(),
                domain.getParticipants().stream()
                        .map(participant -> shoppingListParticipantMapper.toOutput(participant))
                        .toList()
        );
    }

}
