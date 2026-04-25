package com.listaai.list.application.usecase.participants;

import com.listaai.list.application.dto.input.ShoppingListParticipantCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.domain.exception.participant.ParticipantNotFoundException;
import com.listaai.list.domain.model.ShoppingList;
import com.listaai.list.domain.model.ShoppingListParticipant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateParticipantFromListServiceTest {

    @Mock
    private ShoppingListRepositoryPort shoppingListRepositoryPort;

    @Mock
    private ShoppingListMapper shoppingListMapper;

    @InjectMocks
    private UpdateParticipantFromListService updateParticipantFromListService;

    private final Long listId = 1L;
    private final Long participantId = 1L;

    private ShoppingList shoppingList;
    private ShoppingList savedShoppingList;
    private ShoppingListOutput shoppingListOutput;
    private ShoppingListParticipantCommand command;

    @BeforeEach
    void setUp() {
        shoppingList = new ShoppingList(
                listId,
                "Lista teste com participante",
                List.of(),
                List.of(new ShoppingListParticipant(participantId, "Participante teste", "11999990001"))
        );
        savedShoppingList = new ShoppingList(
                listId,
                "Lista teste com participante atualizado",
                List.of(),
                List.of(new ShoppingListParticipant(participantId, "Participante atualizado", "11111111111"))
        );
        shoppingListOutput = new ShoppingListOutput(
                listId,
                "Lista teste com participante atualizado",
                List.of(),
                List.of()
        );
        command = new ShoppingListParticipantCommand("Participante atualizado", "11111111111");
    }

    @Test
    void shouldUpdateParticipantAndPersistUpdatedList() {
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = updateParticipantFromListService
                .updateParticipantFromShoppingListUseCase(listId, participantId, command);

        assertSame(shoppingListOutput, result);
        assertEquals("Participante atualizado", shoppingList.getParticipants().getFirst().getName());
        assertEquals("11111111111", shoppingList.getParticipants().getFirst().getPhoneNumber());

        var inOrder = inOrder(shoppingListRepositoryPort, shoppingListMapper);
        inOrder.verify(shoppingListRepositoryPort).findById(listId);
        inOrder.verify(shoppingListRepositoryPort).save(shoppingList);
        inOrder.verify(shoppingListMapper).toOutput(savedShoppingList);
    }

    @Test
    void shouldKeepExistingPhoneNumberWhenCommandPhoneNumberIsNull() {
        command = new ShoppingListParticipantCommand("Participante atualizado", null);
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = updateParticipantFromListService
                .updateParticipantFromShoppingListUseCase(listId, participantId, command);

        assertSame(shoppingListOutput, result);
        assertEquals("Participante atualizado", shoppingList.getParticipants().getFirst().getName());
        assertEquals("11999990001", shoppingList.getParticipants().getFirst().getPhoneNumber());
    }

    @Test
    void shouldThrowWhenListDoesNotExist() {
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.empty());

        ShoppingListNotFoundException exception = assertThrows(
                ShoppingListNotFoundException.class,
                () -> updateParticipantFromListService
                        .updateParticipantFromShoppingListUseCase(listId, participantId, command)
        );

        assertEquals("Shopping list not found", exception.getMessage());
        verify(shoppingListRepositoryPort, never()).save(shoppingList);
        verify(shoppingListMapper, never()).toOutput(savedShoppingList);
    }

    @Test
    void shouldThrowWhenParticipantDoesNotExist() {
        shoppingList = new ShoppingList(listId, "Lista teste sem participante", List.of(), List.of());
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));

        ParticipantNotFoundException exception = assertThrows(
                ParticipantNotFoundException.class,
                () -> updateParticipantFromListService
                        .updateParticipantFromShoppingListUseCase(listId, participantId, command)
        );

        assertEquals("Participant not found", exception.getMessage());
        verify(shoppingListRepositoryPort, never()).save(shoppingList);
        verify(shoppingListMapper, never()).toOutput(savedShoppingList);
    }

}
