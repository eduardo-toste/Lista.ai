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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateShoppingListServiceTest {

    @Mock
    private ShoppingListRepositoryPort shoppingListRepositoryPort;

    @Mock
    private ShoppingListMapper shoppingListMapper;

    @InjectMocks
    private CreateShoppingListService createShoppingListService;

    private ShoppingListItemCommand itemCommand;
    private ShoppingListParticipantCommand participantCommand;
    private ShoppingListParticipantOutput shoppingListParticipantOutput;
    private ShoppingListItemOutput shoppingListItemOutput;
    private ShoppingListParticipant shoppingListParticipant;
    private ShoppingListItem shoppingListItem;

    @BeforeEach
    void setUp() {
        itemCommand = new ShoppingListItemCommand(
                "Arroz",
                2,
                ItemUnit.KG
        );

        participantCommand = new ShoppingListParticipantCommand(
                "Participante teste",
                "11999990001"
        );

        shoppingListParticipantOutput = new ShoppingListParticipantOutput(
                1L,
                "Participante teste",
                "11999990001"
        );

        shoppingListItemOutput = new ShoppingListItemOutput(
                1L,
                "Arroz",
                2,
                ItemUnit.KG,
                false
        );

        shoppingListParticipant = new ShoppingListParticipant(
                1L,
                "Participante teste",
                "11999990001"
        );

        shoppingListItem = new ShoppingListItem(
                1L,
                "Arroz",
                2,
                ItemUnit.KG
        );
    }

    @Test
    void shouldCreateCompleteShoppingListSuccessfully() {
        CreateShoppingListCommand shoppingListCommand = new CreateShoppingListCommand(
                "Lista populada",
                new ArrayList<>(List.of(itemCommand)),
                new ArrayList<>(List.of(participantCommand))
        );

        ShoppingListOutput shoppingListOutput = new ShoppingListOutput(
                1L,
                "Lista populada",
                new ArrayList<>(List.of(shoppingListItemOutput)),
                new ArrayList<>(List.of(shoppingListParticipantOutput))
        );

        ShoppingList shoppingList = new ShoppingList(
                1L,
                "Lista populada",
                new ArrayList<>(List.of(shoppingListItem)),
                new ArrayList<>(List.of(shoppingListParticipant))
        );

        ShoppingList savedShoppingList = new ShoppingList(
                1L,
                "Lista populada",
                new ArrayList<>(List.of(shoppingListItem)),
                new ArrayList<>(List.of(shoppingListParticipant))
        );
        when(shoppingListMapper.toDomain(shoppingListCommand)).thenReturn(shoppingList);
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = createShoppingListService.createShoppingList(shoppingListCommand);

        assertEquals(1L, result.id());
        assertEquals("Lista populada", result.name());
        assertEquals(1L, result.items().getFirst().id());
        assertEquals("Arroz", result.items().getFirst().name());
        assertEquals(1L, result.participants().getFirst().id());
        assertEquals("Participante teste", result.participants().getFirst().name());

        verify(shoppingListMapper).toDomain(any());
        verify(shoppingListRepositoryPort).save(any());
        verify(shoppingListMapper).toOutput(any());
    }

    @Test
    void shouldCreateShoppingListWithoutItemAndParticipantSuccessfully() {
        CreateShoppingListCommand shoppingListCommand = new CreateShoppingListCommand(
                "Lista sem item e participante",
                new ArrayList<>(List.of()),
                new ArrayList<>(List.of())
        );

        ShoppingListOutput shoppingListOutput = new ShoppingListOutput(
                1L,
                "Lista sem item e participante",
                new ArrayList<>(List.of()),
                new ArrayList<>(List.of())
        );

        ShoppingList shoppingList = new ShoppingList(
                1L,
                "Lista sem item e participante",
                new ArrayList<>(List.of()),
                new ArrayList<>(List.of())
        );

        ShoppingList savedShoppingList = new ShoppingList(
                1L,
                "Lista sem item e participante",
                new ArrayList<>(List.of()),
                new ArrayList<>(List.of())
        );
        when(shoppingListMapper.toDomain(shoppingListCommand)).thenReturn(shoppingList);
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = createShoppingListService.createShoppingList(shoppingListCommand);

        assertEquals(1L, result.id());
        assertEquals("Lista sem item e participante", result.name());
        assertEquals(0, result.items().size());
        assertEquals(0, result.participants().size());

        verify(shoppingListMapper).toDomain(any());
        verify(shoppingListRepositoryPort).save(any());
        verify(shoppingListMapper).toOutput(any());
    }

}