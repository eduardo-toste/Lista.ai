package com.listaai.list.application.port.inbound.participants;

import com.listaai.list.application.dto.output.ShoppingListOutput;

public interface RemoveParticipantFromListUseCase {

    ShoppingListOutput removeParticipantFromShoppingList(Long listId, Long participantId);

}
