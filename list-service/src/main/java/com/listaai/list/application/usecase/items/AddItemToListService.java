package com.listaai.list.application.usecase.items;

import com.listaai.list.application.dto.input.ShoppingListItemCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.mapper.ShoppingListItemMapper;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.inbound.items.AddItemToListUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.utils.ShoppingListUtils;
import com.listaai.list.domain.model.ShoppingList;
import com.listaai.list.domain.model.ShoppingListItem;

public class AddItemToListService implements AddItemToListUseCase {

    private final ShoppingListRepositoryPort shoppingListRepositoryPort;
    private final ShoppingListMapper shoppingListMapper;
    private final ShoppingListItemMapper shoppingListItemMapper;

    public AddItemToListService(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper, ShoppingListItemMapper shoppingListItemMapper) {
        this.shoppingListRepositoryPort = shoppingListRepositoryPort;
        this.shoppingListMapper = shoppingListMapper;
        this.shoppingListItemMapper = shoppingListItemMapper;
    }

    @Override
    public ShoppingListOutput addItemToShoppingList(Long listId, ShoppingListItemCommand command) {
        ShoppingList shoppingList = ShoppingListUtils.findListOrThrow(shoppingListRepositoryPort, listId);
        ShoppingListItem item = shoppingListItemMapper.toDomain(command);

        shoppingList.addItem(item);
        ShoppingList savedShoppingList = shoppingListRepositoryPort.save(shoppingList);

        return shoppingListMapper.toOutput(savedShoppingList);
    }

}
