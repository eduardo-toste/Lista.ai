package com.listaai.list.application.usecase.participants;

import com.listaai.list.application.dto.input.ShoppingListParticipantCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.dto.output.ShoppingListParticipantOutput;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateParticipantFromListServiceTest {

    @Mock
    private ShoppingListRepositoryPort shoppingListRepositoryPort;

    @Mock
    private ShoppingListMapper shoppingListMapper;

    @InjectMocks
    private UpdateParticipantFromListService updateParticipantFromListService ;

    private Long listId = 1L;
    private Long participantId = 1L;

    private ShoppingList shoppingList;
    private ShoppingList savedShoppingList;
    private ShoppingListParticipant shoppingListParticipant;
    private ShoppingListParticipantOutput shoppingListParticipantOutput;
    private ShoppingListOutput shoppingListOutput;
    private ShoppingListParticipantCommand command;

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
                "Participante atualizado",
                "11111111111"
        );

        shoppingListOutput = new ShoppingListOutput(
                1L,
                "Lista teste com participante",
                new ArrayList<>(List.of()),
                new ArrayList<>(List.of(shoppingListParticipantOutput))
        );

        command = new ShoppingListParticipantCommand(
                "Participante atualizado",
                "11111111111"
        );
    }

    @Test
    void shouldUpdateParticipantFromShoppingListSuccessfully() {
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = updateParticipantFromListService.updateParticipantFromShoppingListUseCase(listId, participantId, command);

        assertEquals("Participante atualizado", result.participants().getFirst().name());
        assertEquals("11111111111", result.participants().getFirst().phoneNumber());
    }

    @Test
    void shouldUpdateParticipantFromShoppingListSuccessfullyWithOneFieldNull() {
        shoppingListParticipantOutput = new ShoppingListParticipantOutput(
                1L,
                "Participante atualizado",
                "11999990001"
        );

        shoppingListOutput = new ShoppingListOutput(
                1L,
                "Lista teste com participante",
                new ArrayList<>(List.of()),
                new ArrayList<>(List.of(shoppingListParticipantOutput))
        );

        command = new ShoppingListParticipantCommand(
                "Participante atualizado",
                null
        );

        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = updateParticipantFromListService.updateParticipantFromShoppingListUseCase(listId, participantId, command);

        assertEquals("Participante atualizado", result.participants().getFirst().name());
        assertEquals("11999990001", result.participants().getFirst().phoneNumber());
    }

    @Test
    void shouldThrowExceptionWhenListNotFound() {
        Long listId = 99L;
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                updateParticipantFromListService.updateParticipantFromShoppingListUseCase(listId, participantId, command)
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
                () -> updateParticipantFromListService.updateParticipantFromShoppingListUseCase(listId, participantId, command));

        assertEquals("Participant not found", ex.getMessage());

        verify(shoppingListRepositoryPort, never()).save(any());
        verify(shoppingListMapper, never()).toOutput(any());
    }

}