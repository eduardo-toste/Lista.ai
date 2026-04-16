package com.listaai.list.application.port.inbound.lists;

import com.listaai.list.application.dto.input.CreateShoppingListCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;

public interface CreateShoppingListUseCase {

    ShoppingListOutput createShoppingList(CreateShoppingListCommand command);

}
