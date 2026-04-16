package com.listaai.list.application.utils;

import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.domain.model.ShoppingList;

public class ShoppingListUtils {

    public static ShoppingList findListOrThrow(ShoppingListRepositoryPort repository, Long listId) {
        return repository.findById(listId)
                .orElseThrow(ShoppingListNotFoundException::new);
    }

}
