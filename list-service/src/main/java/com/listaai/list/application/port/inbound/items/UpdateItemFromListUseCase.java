package com.listaai.list.application.port.inbound.items;

import com.listaai.list.application.dto.input.ShoppingListItemCommand;
import com.listaai.list.application.dto.output.ShoppingListItemOutput;
import com.listaai.list.application.dto.output.ShoppingListOutput;

public interface UpdateItemFromListUseCase {

    ShoppingListOutput updateItem(Long listId, Long itemId, ShoppingListItemCommand command);

}
