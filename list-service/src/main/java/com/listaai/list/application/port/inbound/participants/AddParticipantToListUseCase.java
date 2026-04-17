package com.listaai.list.application.port.inbound.participants;

import com.listaai.list.application.dto.input.ShoppingListParticipantCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;

public interface AddParticipantToListUseCase {

    ShoppingListOutput addParticipantToShoppingList(Long listId, ShoppingListParticipantCommand command);

}
