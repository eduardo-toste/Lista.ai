package com.listaai.list.application.usecase.lists;

import com.listaai.list.application.port.inbound.lists.ShareShoppingListUseCase;
import com.listaai.list.application.port.outbound.ShoppingListEventPublisherPort;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.utils.ShoppingListUtils;
import com.listaai.list.domain.model.ShoppingList;

public class ShareShoppingListService implements ShareShoppingListUseCase {

    private final ShoppingListRepositoryPort shoppingListRepositoryPort;
    private final ShoppingListEventPublisherPort shoppingListEventPublisherPort;

    public ShareShoppingListService(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListEventPublisherPort shoppingListEventPublisherPort) {
        this.shoppingListRepositoryPort = shoppingListRepositoryPort;
        this.shoppingListEventPublisherPort = shoppingListEventPublisherPort;
    }

    @Override
    public void shareShoppingList(Long listId) {
        ShoppingList shoppingList = ShoppingListUtils.findListOrThrow(shoppingListRepositoryPort, listId);
        shoppingList.validateCanBeShared();

        shoppingListEventPublisherPort.publishShoppingListShared(shoppingList);
    }
}
