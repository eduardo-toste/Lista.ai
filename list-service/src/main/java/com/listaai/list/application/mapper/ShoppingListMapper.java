package com.listaai.list.application.mapper;

import com.listaai.list.application.dto.input.ShoppingListCommand;
import com.listaai.list.application.dto.input.SmartShoppingListCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.domain.model.ShoppingList;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ShoppingListMapper {

    private final ShoppingListItemMapper shoppingListItemMapper;
    private final ShoppingListParticipantMapper shoppingListParticipantMapper;

    public ShoppingListMapper(ShoppingListItemMapper shoppingListItemMapper, ShoppingListParticipantMapper shoppingListParticipantMapper) {
        this.shoppingListItemMapper = shoppingListItemMapper;
        this.shoppingListParticipantMapper = shoppingListParticipantMapper;
    }

    public ShoppingList toDomain(ShoppingListCommand command) {
        return new ShoppingList(
                null,
                command.name(),
                command.items().stream()
                        .map(shoppingListItemMapper::toDomain)
                        .toList(),
                command.participants().stream()
                        .map(shoppingListParticipantMapper::toDomain)
                        .toList()
        );
    }

    public ShoppingList toDomain(SmartShoppingListCommand command) {
        return new ShoppingList(
                null,
                command.name(),
                new ArrayList<>(),
                command.participants().stream()
                        .map(shoppingListParticipantMapper::toDomain)
                        .toList()
        );
    }

    public ShoppingListOutput toOutput(ShoppingList domain) {
        return new ShoppingListOutput(
                domain.getId(),
                domain.getName(),
                domain.getItems().stream()
                        .map(shoppingListItemMapper::toOutput)
                        .toList(),
                domain.getParticipants().stream()
                        .map(shoppingListParticipantMapper::toOutput)
                        .toList()
        );
    }

    public Page<ShoppingListOutput> toPageOutput(Page<ShoppingList> page) {
        return page.map(this::toOutput);
    }

}
