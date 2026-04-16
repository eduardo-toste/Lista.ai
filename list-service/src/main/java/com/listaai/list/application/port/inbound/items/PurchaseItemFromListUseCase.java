package com.listaai.list.application.port.inbound.items;

import com.listaai.list.application.dto.output.ShoppingListOutput;

public interface PurchaseItemFromListUseCase {

    ShoppingListOutput purchaseItemFromList(Long listId, Long itemId, boolean purchased);

}
