package com.listaai.list.application.usecase.participants;

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

import java.util.ArrayList;
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
class RemoveParticipantFromListServiceTest {

    @Mock
    private ShoppingListRepositoryPort shoppingListRepositoryPort;

    @Mock
    private ShoppingListMapper shoppingListMapper;

    @InjectMocks
    private RemoveParticipantFromListService removeParticipantFromListService;

    private final Long listId = 1L;
    private final Long participantId = 1L;

    private ShoppingList shoppingList;
    private ShoppingList savedShoppingList;
    private ShoppingListOutput shoppingListOutput;

    @BeforeEach
    void setUp() {
        shoppingList = new ShoppingList(
                listId,
                "Lista teste com participante",
                new ArrayList<>(),
                new ArrayList<>(List.of(new ShoppingListParticipant(participantId, "Participante teste", "11999990001")))
        );
        savedShoppingList = new ShoppingList(listId, "Lista teste sem participante", List.of(), List.of());
        shoppingListOutput = new ShoppingListOutput(listId, "Lista teste sem participante", List.of(), List.of());
    }

    @Test
    void shouldRemoveParticipantAndPersistUpdatedList() {
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = removeParticipantFromListService.removeParticipantFromShoppingList(listId, participantId);

        assertSame(shoppingListOutput, result);
        assertEquals(0, shoppingList.getParticipants().size());

        var inOrder = inOrder(shoppingListRepositoryPort, shoppingListMapper);
        inOrder.verify(shoppingListRepositoryPort).findById(listId);
        inOrder.verify(shoppingListRepositoryPort).save(shoppingList);
        inOrder.verify(shoppingListMapper).toOutput(savedShoppingList);
    }

    @Test
    void shouldThrowWhenListDoesNotExist() {
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.empty());

        ShoppingListNotFoundException exception = assertThrows(
                ShoppingListNotFoundException.class,
                () -> removeParticipantFromListService.removeParticipantFromShoppingList(listId, participantId)
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
                () -> removeParticipantFromListService.removeParticipantFromShoppingList(listId, participantId)
        );

        assertEquals("Participant not found", exception.getMessage());
        verify(shoppingListRepositoryPort, never()).save(shoppingList);
        verify(shoppingListMapper, never()).toOutput(savedShoppingList);
    }

}
