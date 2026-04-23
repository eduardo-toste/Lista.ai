package com.listaai.list.application.usecase.items;

import com.listaai.list.application.dto.input.ShoppingListItemCommand;
import com.listaai.list.application.dto.output.ShoppingListItemOutput;
import com.listaai.list.application.dto.output.ShoppingListOutput;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private ShoppingList shoppingList;
    private ShoppingList savedShoppingList;
    private ShoppingListItem shoppingListItem;
    private ShoppingListItemOutput shoppingListItemOutput;
    private ShoppingListOutput shoppingListOutput;
    private ShoppingListItemCommand command;

    @BeforeEach
    void setUp() {
        shoppingList = new ShoppingList(
                1L,
                "Lista teste vazia",
                new ArrayList<>(List.of()),
                new ArrayList<>(List.of())
        );

        shoppingListItem = new ShoppingListItem(
                1L,
                "Arroz",
                2,
                ItemUnit.KG
        );

        savedShoppingList = new ShoppingList(
                1L,
                "Lista teste com item",
                new ArrayList<>(List.of(shoppingListItem)),
                new ArrayList<>(List.of())
        );

        shoppingListItemOutput = new ShoppingListItemOutput(
                1L,
                "Arroz",
                2,
                ItemUnit.KG,
                false
        );

        shoppingListOutput = new ShoppingListOutput(
                1L,
                "Lista teste com item",
                new ArrayList<>(List.of(shoppingListItemOutput)),
                new ArrayList<>(List.of())
        );

        command = new ShoppingListItemCommand(
                "Arroz",
                2,
                ItemUnit.KG
        );
    }

    @Test
    void shouldAddItemToListSuccessfully() {
        Long listId = 1L;
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListItemMapper.toDomain(command)).thenReturn(shoppingListItem);
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = addItemToListService.addItemToShoppingList(listId, command);

        assertEquals(1, result.items().size());
        assertEquals("Arroz", result.items().getFirst().name());
        assertEquals(2, result.items().getFirst().quantity());
        assertEquals(ItemUnit.KG, result.items().getFirst().unit());
    }

    @Test
    void shouldThrowExceptionWhenListNotFound() {
        Long listId = 99L;
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                addItemToListService.addItemToShoppingList(listId, command)
        );

        assertEquals("Shopping list not found", ex.getMessage());

        verify(shoppingListItemMapper, never()).toDomain(any());
        verify(shoppingListRepositoryPort, never()).save(any());
        verify(shoppingListMapper, never()).toOutput(any());
    }

    @Test
    void shouldThrowExceptionWhenItemAlreadyExists() {
        Long listId = 1L;
        shoppingList = new ShoppingList(
                1L,
                "Lista teste com item",
                new ArrayList<>(List.of(new ShoppingListItem(2L, "Arroz", 1, ItemUnit.UN))),
                new ArrayList<>(List.of())
        );
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListItemMapper.toDomain(command)).thenReturn(shoppingListItem);

        RuntimeException ex = assertThrows(ItemAlreadyAddedException.class,
                () -> addItemToListService.addItemToShoppingList(listId, command));

        assertEquals("Item already exists", ex.getMessage());

        verify(shoppingListRepositoryPort, never()).save(any());
        verify(shoppingListMapper, never()).toOutput(any());
    }

}
