package com.listaai.list.application.usecase.participants;

import com.listaai.list.application.dto.input.ShoppingListParticipantCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.mapper.ShoppingListParticipantMapper;
import com.listaai.list.application.port.inbound.participants.AddParticipantToListUseCase;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.utils.ShoppingListUtils;
import com.listaai.list.domain.model.ShoppingList;

public class AddParticipantToListService implements AddParticipantToListUseCase {

    private final ShoppingListRepositoryPort shoppingListRepositoryPort;
    private final ShoppingListMapper shoppingListMapper;
    private final ShoppingListParticipantMapper shoppingListParticipantMapper;

    public AddParticipantToListService(ShoppingListRepositoryPort shoppingListRepositoryPort, ShoppingListMapper shoppingListMapper, ShoppingListParticipantMapper shoppingListParticipantMapper) {
        this.shoppingListRepositoryPort = shoppingListRepositoryPort;
        this.shoppingListMapper = shoppingListMapper;
        this.shoppingListParticipantMapper = shoppingListParticipantMapper;
    }

    @Override
    public ShoppingListOutput addParticipantToShoppingList(Long listId, ShoppingListParticipantCommand command) {
        ShoppingList shoppingList = ShoppingListUtils.findListOrThrow(shoppingListRepositoryPort, listId);
        shoppingList.addParticipant(shoppingListParticipantMapper.toDomain(command));
        ShoppingList savedShoppingList = shoppingListRepositoryPort.save(shoppingList);
        return shoppingListMapper.toOutput(savedShoppingList);
    }

}
