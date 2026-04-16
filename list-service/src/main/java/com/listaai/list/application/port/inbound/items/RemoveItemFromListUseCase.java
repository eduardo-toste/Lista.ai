package com.listaai.list.application.port.inbound.items;

import com.listaai.list.application.dto.input.ShoppingListItemCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;

public interface RemoveItemFromListUseCase {

    ShoppingListOutput removeItemFromShoppingList(Long listId, Long itemId);

}
