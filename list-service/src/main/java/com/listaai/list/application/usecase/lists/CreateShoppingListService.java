package com.listaai.list.application.usecase.lists;

import com.listaai.list.application.dto.input.CreateShoppingListCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.inbound.lists.CreateShoppingListUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.domain.model.ShoppingList;

public class CreateShoppingListService implements CreateShoppingListUseCase {

    private ShoppingListRepositoryPort shoppingListRepositoryPort;
    private ShoppingListMapper shoppingListMapper;

    public CreateShoppingListService(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper) {
        this.shoppingListRepositoryPort = shoppingListRepositoryPort;
        this.shoppingListMapper = shoppingListMapper;
    }

    @Override
    public ShoppingListOutput createShoppingList(CreateShoppingListCommand command) {
        ShoppingList shoppingList = shoppingListMapper.toDomain(command);
        ShoppingList savedShoppingList = shoppingListRepositoryPort.save(shoppingList);
        return shoppingListMapper.toOutput(savedShoppingList);
    }

}
