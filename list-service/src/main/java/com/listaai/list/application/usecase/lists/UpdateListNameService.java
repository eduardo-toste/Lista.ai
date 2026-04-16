package com.listaai.list.application.usecase.lists;

import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.inbound.lists.UpdateListNameUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.utils.ShoppingListUtils;
import com.listaai.list.domain.model.ShoppingList;

public class UpdateListNameService implements UpdateListNameUseCase {

    private final ShoppingListRepositoryPort shoppingListRepositoryPort;
    private final ShoppingListMapper shoppingListMapper;

    public UpdateListNameService(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper) {
        this.shoppingListRepositoryPort = shoppingListRepositoryPort;
        this.shoppingListMapper = shoppingListMapper;
    }

    @Override
    public ShoppingListOutput updateName(Long id, String name) {
        ShoppingList shoppingList = ShoppingListUtils.findListOrThrow(shoppingListRepositoryPort, id);

        shoppingList.updateListName(name);
        ShoppingList savedShoppingList =  shoppingListRepositoryPort.save(shoppingList);

        return shoppingListMapper.toOutput(savedShoppingList);
    }

}
