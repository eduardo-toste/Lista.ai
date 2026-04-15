package com.listaai.list.application.port.inbound;

import com.listaai.list.application.dto.output.ShoppingListOutput;

public interface UpdateListNameUseCase {

    ShoppingListOutput updateName(Long id, String name);

}
