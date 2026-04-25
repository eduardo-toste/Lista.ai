package com.listaai.list.application.usecase.items;

import com.listaai.list.application.dto.input.ShoppingListItemCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.application.mapper.ShoppingListItemMapper;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.exception.item.ItemAlreadyAddedException;
import com.listaai.list.domain.model.ShoppingList;
import com.listaai.list.domain.model.ShoppingListItem;
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
class AddItemToListServiceTest {

    @Mock
    private ShoppingListRepositoryPort shoppingListRepositoryPort;

    @Mock
    private ShoppingListMapper shoppingListMapper;

    @Mock
    private ShoppingListItemMapper shoppingListItemMapper;

    @InjectMocks
    private AddItemToListService addItemToListService;

    private final Long listId = 1L;

    private ShoppingList shoppingList;
    private ShoppingListItem mappedItem;
    private ShoppingList savedShoppingList;
    private ShoppingListOutput shoppingListOutput;
    private ShoppingListItemCommand command;

    @BeforeEach
    void setUp() {
        shoppingList = new ShoppingList(
                listId,
                "Lista teste vazia",
                new ArrayList<>(),
                new ArrayList<>()
        );
        mappedItem = new ShoppingListItem(
                null,
                "Arroz",
                2,
                ItemUnit.KG
        );
        savedShoppingList = new ShoppingList(
                listId,
                "Lista teste com item",
                List.of(new ShoppingListItem(10L, "Arroz", 2, ItemUnit.KG)),
                List.of()
        );
        shoppingListOutput = new ShoppingListOutput(
                listId,
                "Lista teste com item",
                List.of(),
                List.of()
        );
        command = new ShoppingListItemCommand("Arroz", 2, ItemUnit.KG);
    }

    @Test
    void shouldAddItemToListAndPersistUpdatedList() {
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListItemMapper.toDomain(command)).thenReturn(mappedItem);
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = addItemToListService.addItemToShoppingList(listId, command);

        assertSame(shoppingListOutput, result);
        assertEquals(1, shoppingList.getItems().size());
        assertSame(mappedItem, shoppingList.getItems().getFirst());

        var inOrder = inOrder(shoppingListRepositoryPort, shoppingListItemMapper, shoppingListMapper);
        inOrder.verify(shoppingListRepositoryPort).findById(listId);
        inOrder.verify(shoppingListItemMapper).toDomain(command);
        inOrder.verify(shoppingListRepositoryPort).save(shoppingList);
        inOrder.verify(shoppingListMapper).toOutput(savedShoppingList);
    }

    @Test
    void shouldThrowWhenListDoesNotExist() {
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.empty());

        ShoppingListNotFoundException exception = assertThrows(
                ShoppingListNotFoundException.class,
                () -> addItemToListService.addItemToShoppingList(listId, command)
        );

        assertEquals("Shopping list not found", exception.getMessage());
        verify(shoppingListItemMapper, never()).toDomain(command);
        verify(shoppingListRepositoryPort, never()).save(shoppingList);
        verify(shoppingListMapper, never()).toOutput(savedShoppingList);
    }

    @Test
    void shouldThrowWhenItemAlreadyExists() {
        shoppingList = new ShoppingList(
                listId,
                "Lista teste com item",
                List.of(new ShoppingListItem(2L, "Arroz", 1, ItemUnit.UN)),
                new ArrayList<>()
        );
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListItemMapper.toDomain(command)).thenReturn(mappedItem);

        ItemAlreadyAddedException exception = assertThrows(
                ItemAlreadyAddedException.class,
                () -> addItemToListService.addItemToShoppingList(listId, command)
        );

        assertEquals("Item already exists", exception.getMessage());
        verify(shoppingListRepositoryPort, never()).save(shoppingList);
        verify(shoppingListMapper, never()).toOutput(savedShoppingList);
    }

}
