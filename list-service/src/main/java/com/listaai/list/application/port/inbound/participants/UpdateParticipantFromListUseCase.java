package com.listaai.list.application.port.inbound.participants;

import com.listaai.list.application.dto.input.ShoppingListParticipantCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;

public interface UpdateParticipantFromListUseCase {

    ShoppingListOutput updateParticipantFromShoppingListUseCase(Long listId, Long participantId, ShoppingListParticipantCommand command);

}
