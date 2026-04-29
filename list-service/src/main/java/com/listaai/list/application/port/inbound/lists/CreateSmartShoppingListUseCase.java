package com.listaai.list.application.port.inbound.lists;

import com.listaai.list.application.dto.input.SmartShoppingListCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;

public interface CreateSmartShoppingListUseCase {

    ShoppingListOutput createSmartShoppingList(SmartShoppingListCommand command);

}
