package com.listaai.list.application.usecase.items;

import com.listaai.list.adapter.inbound.web.mapper.ShoppingListItemWebMapper;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.application.mapper.ShoppingListItemMapper;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.inbound.items.PurchaseItemFromListUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
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
        ShoppingList shoppingList = shoppingListRepositoryPort.findById(listId)
                .orElseThrow(ShoppingListNotFoundException::new);

        if (purchased) {
            shoppingList.markItemAsPurchased(itemId);
        } else {
            shoppingList.unmarkItemAsPurchased(itemId);
        }

        ShoppingList savedShoppingList = shoppingListRepositoryPort.save(shoppingList);

        return shoppingListMapper.toOutput(savedShoppingList);
    }

}
