package com.listaai.list.application.usecase.items;

import com.listaai.list.application.dto.input.ShoppingListItemCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.inbound.items.UpdateItemFromListUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.utils.ShoppingListUtils;
import com.listaai.list.domain.model.ShoppingList;

public class UpdateItemFromListService implements UpdateItemFromListUseCase {

    private final ShoppingListRepositoryPort shoppingListRepositoryPort;
    private final ShoppingListMapper shoppingListMapper;

    public UpdateItemFromListService(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper) {
        this.shoppingListRepositoryPort = shoppingListRepositoryPort;
        this.shoppingListMapper = shoppingListMapper;
    }

    @Override
    public ShoppingListOutput updateItem(Long listId, Long itemId, ShoppingListItemCommand command) {
        ShoppingList shoppingList = ShoppingListUtils.findListOrThrow(shoppingListRepositoryPort, listId);

        shoppingList.updateItem(itemId, command.name(), command.quantity(), command.unit());
        ShoppingList savedShoppingList = shoppingListRepositoryPort.save(shoppingList);

        return shoppingListMapper.toOutput(savedShoppingList);
    }

}
