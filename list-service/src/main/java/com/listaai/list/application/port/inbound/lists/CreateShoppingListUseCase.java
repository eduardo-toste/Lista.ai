package com.listaai.list.application.port.inbound.lists;

import com.listaai.list.application.dto.input.ShoppingListCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;

public interface CreateShoppingListUseCase {

    ShoppingListOutput createShoppingList(ShoppingListCommand command);

}
