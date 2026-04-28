package com.listaai.notification.application.port.inbound;

import com.listaai.notification.application.dto.input.HandleShoppingListSharedCommand;

public interface HandleShoppingListSharedUseCase {

    void handle(HandleShoppingListSharedCommand command);

}
