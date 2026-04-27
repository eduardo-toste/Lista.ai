package com.listaai.list.application.port.outbound;

import com.listaai.list.domain.model.ShoppingList;

public interface ShoppingListEventPublisherPort {

    void publishShoppingListShared(ShoppingList shoppingList);

}
