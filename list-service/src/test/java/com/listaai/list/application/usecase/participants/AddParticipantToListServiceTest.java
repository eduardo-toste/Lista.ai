package com.listaai.list.application.usecase.participants;

import com.listaai.list.application.dto.input.ShoppingListParticipantCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.dto.output.ShoppingListParticipantOutput;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.mapper.ShoppingListParticipantMapper;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.application.utils.ShoppingListUtils;
import com.listaai.list.domain.exception.participant.ParticipantAlreadyAddedException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddParticipantToListServiceTest {

    @Mock
    private ShoppingListRepositoryPort shoppingListRepositoryPort;

    @Mock
    private ShoppingListMapper shoppingListMapper;

    @Mock
    private ShoppingListParticipantMapper shoppingListParticipantMapper;

    @InjectMocks
    private AddParticipantToListService addParticipantToListService;

    private ShoppingList shoppingList;
    private ShoppingList savedShoppingList;
    private ShoppingListParticipant shoppingListParticipant;
    private ShoppingListOutput shoppingListOutput;
    private ShoppingListParticipantOutput shoppingListParticipantOutput;
    private ShoppingListParticipantCommand command;

    @BeforeEach
    void setUp() {
        shoppingList = new ShoppingList(
                1L,
                "Lista teste vazia",
                new ArrayList<>(List.of()),
                new ArrayList<>(List.of())
        );

        shoppingListParticipant = new ShoppingListParticipant(
                1L,
                "Participante teste",
                "11999990001"
        );

        savedShoppingList = new ShoppingList(
                1L,
                "Lista teste com participante",
                new ArrayList<>(List.of()),
                new ArrayList<>(List.of(shoppingListParticipant))
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
                new ArrayList<>(List.of(shoppingListParticipantOutput))
        );

        command = new ShoppingListParticipantCommand(
                "Participante teste",
                "11999990001"
        );
    }

    @Test
    void shouldAddParticipantToListSuccessfully() {
        Long listId = 1L;
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListParticipantMapper.toDomain(command)).thenReturn(shoppingListParticipant);
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = addParticipantToListService.addParticipantToShoppingList(listId, command);

        assertEquals(1, result.participants().size());
        assertEquals("Participante teste", result.participants().getFirst().name());
        assertEquals("11999990001", result.participants().getFirst().phoneNumber());
    }

    @Test
    void shouldThrowExceptionWhenListNotFound() {
        Long listId = 99L;
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                addParticipantToListService.addParticipantToShoppingList(listId, command)
        );

        assertEquals("Shopping list not found", ex.getMessage());

        verify(shoppingListParticipantMapper, never()).toDomain(any());
        verify(shoppingListRepositoryPort, never()).save(any());
        verify(shoppingListMapper, never()).toOutput(any());
    }

    @Test
    void shouldThrowExceptionWhenParticipantAlreadyExists() {
        Long listId = 1L;
        shoppingList = new ShoppingList(
                1L,
                "Lista teste vazia",
                new ArrayList<>(List.of()),
                new ArrayList<>(List.of(shoppingListParticipant))
        );
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListParticipantMapper.toDomain(command)).thenReturn(shoppingListParticipant);

        RuntimeException ex = assertThrows(ParticipantAlreadyAddedException.class,
                () -> addParticipantToListService.addParticipantToShoppingList(listId, command));

        assertEquals("Participant already exists", ex.getMessage());

        verify(shoppingListRepositoryPort, never()).save(any());
        verify(shoppingListMapper, never()).toOutput(any());
    }

}