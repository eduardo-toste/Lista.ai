package com.listaai.list.application.usecase.lists;

import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.inbound.lists.GetShoppingListUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.utils.ShoppingListUtils;
import com.listaai.list.domain.model.ShoppingList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class GetShoppingListService implements GetShoppingListUseCase {

    private final ShoppingListRepositoryPort shoppingListRepositoryPort;
    private final ShoppingListMapper shoppingListMapper;

    public GetShoppingListService(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper) {
        this.shoppingListRepositoryPort = shoppingListRepositoryPort;
        this.shoppingListMapper = shoppingListMapper;
    }

    @Override
    public ShoppingListOutput getShoppingListById(Long id) {
        ShoppingList savedShoppingList = ShoppingListUtils.findListOrThrow(shoppingListRepositoryPort, id);
        return shoppingListMapper.toOutput(savedShoppingList);
    }

    @Override
    public Page<ShoppingListOutput> getShoppingLists(Pageable pageable) {
        Page<ShoppingList> shoppingLists = shoppingListRepositoryPort.findAll(pageable);
        return shoppingListMapper.toPageOutput(shoppingLists);
    }
}
