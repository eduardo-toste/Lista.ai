package com.listaai.list.application.port.inbound.lists;

import com.listaai.list.application.dto.output.ShoppingListOutput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetShoppingListUseCase {

    ShoppingListOutput getShoppingListById(Long id);
    Page<ShoppingListOutput> getShoppingLists(Pageable pageable);

}
