package com.listaai.list.application.port.in;

import com.listaai.list.application.dto.input.CreateShoppingListCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;

public interface CreateShoppingListUseCase {

    ShoppingListOutput createShoppingList(CreateShoppingListCommand command);

}
