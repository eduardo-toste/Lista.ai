package com.listaai.list.application.usecase.items;

import com.listaai.list.application.dto.input.ShoppingListItemCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.application.mapper.ShoppingListItemMapper;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.inbound.items.RemoveItemFromListUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.utils.ShoppingListUtils;
import com.listaai.list.domain.model.ShoppingList;

public class RemoveItemFromListService implements RemoveItemFromListUseCase {

    private final ShoppingListRepositoryPort shoppingListRepositoryPort;
    private final ShoppingListMapper shoppingListMapper;

    public RemoveItemFromListService(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper) {
        this.shoppingListRepositoryPort = shoppingListRepositoryPort;
        this.shoppingListMapper = shoppingListMapper;
    }

    @Override
    public ShoppingListOutput removeItemFromShoppingList(Long listId, Long itemId) {
        ShoppingList shoppingList = ShoppingListUtils.findListOrThrow(shoppingListRepositoryPort, listId);

        shoppingList.removeItem(itemId);
        ShoppingList savedList = shoppingListRepositoryPort.save(shoppingList);

        return shoppingListMapper.toOutput(savedList);
    }

}
