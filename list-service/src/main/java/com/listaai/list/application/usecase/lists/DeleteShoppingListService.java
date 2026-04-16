package com.listaai.list.application.usecase.lists;

import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.application.port.inbound.lists.DeleteShoppingListUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.utils.ShoppingListUtils;
import com.listaai.list.domain.model.ShoppingList;

public class DeleteShoppingListService implements DeleteShoppingListUseCase {

    private final ShoppingListRepositoryPort shoppingListRepositoryPort;

    public DeleteShoppingListService(ShoppingListRepositoryPort shoppingListRepositoryPort) {
        this.shoppingListRepositoryPort = shoppingListRepositoryPort;
    }

    @Override
    public void delete(Long id) {
        ShoppingListUtils.findListOrThrow(shoppingListRepositoryPort, id);
        shoppingListRepositoryPort.deleteById(id);
    }

}
