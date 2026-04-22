package com.listaai.list.application.usecase.participants;

import com.listaai.list.application.dto.input.ShoppingListParticipantCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.dto.output.ShoppingListParticipantOutput;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.mapper.ShoppingListParticipantMapper;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveParticipantFromListServiceTest {

    @Mock
    private ShoppingListRepositoryPort shoppingListRepositoryPort;

    @Mock
    private ShoppingListMapper shoppingListMapper;

    @InjectMocks
    private RemoveParticipantFromListService removeParticipantFromListService ;

    private Long listId = 1L;
    private Long participantId = 1L;

    private ShoppingList shoppingList;
    private ShoppingList savedShoppingList;
    private ShoppingListParticipant shoppingListParticipant;
    private ShoppingListParticipantOutput shoppingListParticipantOutput;
    private ShoppingListOutput shoppingListOutput;

    @BeforeEach
    void setUp() {
        shoppingListParticipant = new ShoppingListParticipant(
                1L,
                "Participante teste",
                "11999990001"
        );

        shoppingList = new ShoppingList(
                1L,
                "Lista teste com participante",
                new ArrayList<>(List.of()),
                new ArrayList<>(List.of(shoppingListParticipant))
        );

        savedShoppingList = new ShoppingList(
                1L,
                "Lista teste sem participante",
                new ArrayList<>(List.of()),
                new ArrayList<>(List.of())
        );

        shoppingListParticipantOutput = new ShoppingListParticipantOutput(
                1L,
                "Participante teste",
                "11999990001"
        );

        shoppingListOutput = new ShoppingListOutput(
                1L,
                "Lista teste com participante",
                new ArrayList<>(List.of()),
                new ArrayList<>(List.of())
        );
    }

    @Test
    void shouldRemoveParticipantFromShoppingListSuccessfully() {
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = removeParticipantFromListService.removeParticipantFromShoppingList(listId, participantId);

        assertEquals(0, result.participants().size());
    }

    @Test
    void shouldThrowExceptionWhenListNotFound() {
        Long listId = 99L;
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                removeParticipantFromListService.removeParticipantFromShoppingList(listId, participantId)
        );

        assertEquals("Shopping list not found", ex.getMessage());

        verify(shoppingListRepositoryPort, never()).save(any());
        verify(shoppingListMapper, never()).toOutput(any());
    }

    @Test
    void shouldThrowExceptionWhenParticipantNotFound() {
        shoppingList = new ShoppingList(
                1L,
                "Lista teste com participante",
                new ArrayList<>(List.of()),
                new ArrayList<>(List.of())
        );
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));

        var ex = assertThrows(ParticipantNotFoundException.class,
                () -> removeParticipantFromListService.removeParticipantFromShoppingList(listId, participantId));

        assertEquals("Participant not found", ex.getMessage());

        verify(shoppingListRepositoryPort, never()).save(any());
        verify(shoppingListMapper, never()).toOutput(any());
    }

}