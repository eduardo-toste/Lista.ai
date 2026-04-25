package com.listaai.list.application.usecase.participants;

import com.listaai.list.application.dto.input.ShoppingListParticipantCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.mapper.ShoppingListParticipantMapper;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private final Long listId = 1L;

    private ShoppingList shoppingList;
    private ShoppingListParticipant mappedParticipant;
    private ShoppingList savedShoppingList;
    private ShoppingListOutput shoppingListOutput;
    private ShoppingListParticipantCommand command;

    @BeforeEach
    void setUp() {
        shoppingList = new ShoppingList(listId, "Lista teste vazia", new ArrayList<>(), new ArrayList<>());
        mappedParticipant = new ShoppingListParticipant(null, "Participante teste", "11999990001");
        savedShoppingList = new ShoppingList(
                listId,
                "Lista teste com participante",
                new ArrayList<>(),
                new ArrayList<>(List.of(new ShoppingListParticipant(10L, "Participante teste", "11999990001")))
        );
        shoppingListOutput = new ShoppingListOutput(listId, "Lista teste com participante", List.of(), List.of());
        command = new ShoppingListParticipantCommand("Participante teste", "11999990001");
    }

    @Test
    void shouldAddParticipantAndPersistUpdatedList() {
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListParticipantMapper.toDomain(command)).thenReturn(mappedParticipant);
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = addParticipantToListService.addParticipantToShoppingList(listId, command);

        assertSame(shoppingListOutput, result);
        assertEquals(1, shoppingList.getParticipants().size());
        assertSame(mappedParticipant, shoppingList.getParticipants().getFirst());

        var inOrder = inOrder(shoppingListRepositoryPort, shoppingListParticipantMapper, shoppingListMapper);
        inOrder.verify(shoppingListRepositoryPort).findById(listId);
        inOrder.verify(shoppingListParticipantMapper).toDomain(command);
        inOrder.verify(shoppingListRepositoryPort).save(shoppingList);
        inOrder.verify(shoppingListMapper).toOutput(savedShoppingList);
    }

    @Test
    void shouldThrowWhenListDoesNotExist() {
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.empty());

        ShoppingListNotFoundException exception = assertThrows(
                ShoppingListNotFoundException.class,
                () -> addParticipantToListService.addParticipantToShoppingList(listId, command)
        );

        assertEquals("Shopping list not found", exception.getMessage());
        verify(shoppingListParticipantMapper, never()).toDomain(command);
        verify(shoppingListRepositoryPort, never()).save(shoppingList);
        verify(shoppingListMapper, never()).toOutput(savedShoppingList);
    }

    @Test
    void shouldThrowWhenParticipantAlreadyExists() {
        shoppingList = new ShoppingList(
                listId,
                "Lista teste com participante",
                List.of(),
                List.of(new ShoppingListParticipant(10L, "Participante teste", "11999990001"))
        );
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListParticipantMapper.toDomain(command)).thenReturn(mappedParticipant);

        ParticipantAlreadyAddedException exception = assertThrows(
                ParticipantAlreadyAddedException.class,
                () -> addParticipantToListService.addParticipantToShoppingList(listId, command)
        );

        assertEquals("Participant already exists", exception.getMessage());
        verify(shoppingListRepositoryPort, never()).save(shoppingList);
        verify(shoppingListMapper, never()).toOutput(savedShoppingList);
    }

}
