package com.listaai.notification.application.usecase;

import com.listaai.notification.application.dto.input.HandleShoppingListSharedCommand;
import com.listaai.notification.application.port.inbound.HandleShoppingListSharedUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandleShoppingListSharedService implements HandleShoppingListSharedUseCase {

    private static final Logger log = LoggerFactory.getLogger(HandleShoppingListSharedService.class);

    @Override
    public void handle(HandleShoppingListSharedCommand command) {
        log.info(
                "Received shopping list shared event: listId={}, listName={}, participants={}",
                command.shoppingListId(),
                command.shoppingListName(),
                command.participants().size()
        );
    }
}
