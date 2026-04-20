package com.listaai.list.application.usecase.participants;

import com.listaai.list.application.dto.input.ShoppingListParticipantCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.inbound.participants.UpdateParticipantFromListUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.utils.ShoppingListUtils;
import com.listaai.list.domain.model.ShoppingList;

public class UpdateParticipantFromListService implements UpdateParticipantFromListUseCase {

    private final ShoppingListRepositoryPort shoppingListRepositoryPort;
    private final ShoppingListMapper shoppingListMapper;

    public UpdateParticipantFromListService(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper) {
        this.shoppingListRepositoryPort = shoppingListRepositoryPort;
        this.shoppingListMapper = shoppingListMapper;
    }

    @Override
    public ShoppingListOutput updateParticipantFromShoppingListUseCase(Long listId, Long participantId, ShoppingListParticipantCommand command) {
        ShoppingList shoppingList = ShoppingListUtils.findListOrThrow(shoppingListRepositoryPort, listId);

        shoppingList.updateParticipant(participantId, command.name(), command.phoneNumber());
        ShoppingList savedShoppingList = shoppingListRepositoryPort.save(shoppingList);

        return shoppingListMapper.toOutput(savedShoppingList);
    }

}
