package com.listaai.list.application.usecase.lists;

import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.application.port.inbound.lists.DeleteShoppingListUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;

public class DeleteShoppingListService implements DeleteShoppingListUseCase {

    private final ShoppingListRepositoryPort shoppingListRepositoryPort;

    public DeleteShoppingListService(ShoppingListRepositoryPort shoppingListRepositoryPort) {
        this.shoppingListRepositoryPort = shoppingListRepositoryPort;
    }

    @Override
    public void delete(Long id) {
        shoppingListRepositoryPort.findById(id)
                .orElseThrow(ShoppingListNotFoundException::new);

        shoppingListRepositoryPort.deleteById(id);
    }

}
