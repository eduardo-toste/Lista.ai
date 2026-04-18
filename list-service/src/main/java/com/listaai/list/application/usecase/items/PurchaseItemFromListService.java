package com.listaai.list.application.usecase.items;

import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.inbound.items.PurchaseItemFromListUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.utils.ShoppingListUtils;
import com.listaai.list.domain.model.ShoppingList;

public class PurchaseItemFromListService implements PurchaseItemFromListUseCase {

    private final ShoppingListRepositoryPort shoppingListRepositoryPort;
    private final ShoppingListMapper shoppingListMapper;

    public PurchaseItemFromListService(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper) {
        this.shoppingListRepositoryPort = shoppingListRepositoryPort;
        this.shoppingListMapper = shoppingListMapper;
    }

    @Override
    public ShoppingListOutput purchaseItemFromList(Long listId, Long itemId, boolean purchased) {
        ShoppingList shoppingList = ShoppingListUtils.findListOrThrow(shoppingListRepositoryPort, listId);

        if (purchased) {
            shoppingList.markItemAsPurchased(itemId);
        } else {
            shoppingList.unmarkItemAsPurchased(itemId);
        }

        ShoppingList savedShoppingList = shoppingListRepositoryPort.save(shoppingList);

        return shoppingListMapper.toOutput(savedShoppingList);
    }

}
