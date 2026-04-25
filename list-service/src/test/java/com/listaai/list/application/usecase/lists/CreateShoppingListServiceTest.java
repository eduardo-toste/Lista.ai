package com.listaai.list.application.usecase.lists;

import com.listaai.list.application.dto.input.CreateShoppingListCommand;
import com.listaai.list.application.dto.input.ShoppingListItemCommand;
import com.listaai.list.application.dto.input.ShoppingListParticipantCommand;
import com.listaai.list.application.dto.output.ShoppingListItemOutput;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.dto.output.ShoppingListParticipantOutput;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.model.ShoppingList;
import com.listaai.list.domain.model.ShoppingListItem;
import com.listaai.list.domain.model.ShoppingListParticipant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateShoppingListServiceTest {

    @Mock
    private ShoppingListRepositoryPort shoppingListRepositoryPort;

    @Mock
    private ShoppingListMapper shoppingListMapper;

    @InjectMocks
    private CreateShoppingListService createShoppingListService;

    private CreateShoppingListCommand shoppingListCommand;
    private ShoppingList shoppingList;
    private ShoppingList savedShoppingList;
    private ShoppingListOutput shoppingListOutput;

    @BeforeEach
    void setUp() {
        ShoppingListItemCommand itemCommand = new ShoppingListItemCommand(
                "Arroz",
                2,
                ItemUnit.KG
        );
        ShoppingListParticipantCommand participantCommand = new ShoppingListParticipantCommand(
                "Participante teste",
                "11999990001"
        );

        shoppingListCommand = new CreateShoppingListCommand(
                "Lista populada",
                List.of(itemCommand),
                List.of(participantCommand)
        );

        ShoppingListItem shoppingListItem = new ShoppingListItem(
                null,
                "Arroz",
                2,
                ItemUnit.KG
        );
        ShoppingListParticipant shoppingListParticipant = new ShoppingListParticipant(
                null,
                "Participante teste",
                "11999990001"
        );
        shoppingList = new ShoppingList(
                null,
                "Lista populada",
                List.of(shoppingListItem),
                List.of(shoppingListParticipant)
        );

        ShoppingListItem savedItem = new ShoppingListItem(
                1L,
                "Arroz",
                2,
                ItemUnit.KG
        );
        ShoppingListParticipant savedParticipant = new ShoppingListParticipant(
                1L,
                "Participante teste",
                "11999990001"
        );
        savedShoppingList = new ShoppingList(
                1L,
                "Lista populada",
                List.of(savedItem),
                List.of(savedParticipant)
        );

        ShoppingListItemOutput shoppingListItemOutput = new ShoppingListItemOutput(
                1L,
                "Arroz",
                2,
                ItemUnit.KG,
                false
        );
        ShoppingListParticipantOutput shoppingListParticipantOutput = new ShoppingListParticipantOutput(
                1L,
                "Participante teste",
                "11999990001"
        );
        shoppingListOutput = new ShoppingListOutput(
                1L,
                "Lista populada",
                List.of(shoppingListItemOutput),
                List.of(shoppingListParticipantOutput)
        );
    }

    @Test
    void shouldCreateShoppingListUsingMapperAndRepositoryInOrder() {
        when(shoppingListMapper.toDomain(shoppingListCommand)).thenReturn(shoppingList);
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = createShoppingListService.createShoppingList(shoppingListCommand);

        assertSame(shoppingListOutput, result);

        var inOrder = inOrder(shoppingListMapper, shoppingListRepositoryPort);
        inOrder.verify(shoppingListMapper).toDomain(shoppingListCommand);
        inOrder.verify(shoppingListRepositoryPort).save(shoppingList);
        inOrder.verify(shoppingListMapper).toOutput(savedShoppingList);
    }

    @Test
    void shouldPropagateRepositoryExceptionAndNotMapOutput() {
        RuntimeException exception = new RuntimeException("database unavailable");

        when(shoppingListMapper.toDomain(shoppingListCommand)).thenReturn(shoppingList);
        when(shoppingListRepositoryPort.save(shoppingList)).thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> createShoppingListService.createShoppingList(shoppingListCommand));

        assertSame(exception, thrown);
        verify(shoppingListMapper).toDomain(shoppingListCommand);
        verify(shoppingListRepositoryPort).save(shoppingList);
        verify(shoppingListMapper, never()).toOutput(savedShoppingList);
    }

}
