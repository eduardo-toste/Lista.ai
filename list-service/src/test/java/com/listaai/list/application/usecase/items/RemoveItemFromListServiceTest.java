package com.listaai.list.application.usecase.items;

import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.exception.item.ItemNotFoundException;
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
class RemoveItemFromListServiceTest {

    @Mock
    private ShoppingListRepositoryPort shoppingListRepositoryPort;

    @Mock
    private ShoppingListMapper shoppingListMapper;

    @InjectMocks
    private RemoveItemFromListService removeItemFromListService;

    private Long listId = 1L;
    private Long itemId = 1L;

    private ShoppingList shoppingList;
    private ShoppingList savedShoppingList;
    private ShoppingListOutput shoppingListOutput;

    @BeforeEach
    void setUp() {
        shoppingList = new ShoppingList(
                listId,
                "Lista teste com item",
                new ArrayList<>(List.of(new ShoppingListItem(itemId, "Arroz", 2, ItemUnit.KG))),
                new ArrayList<>(List.of())
        );

        savedShoppingList = new ShoppingList(
                listId,
                "Lista teste sem item",
                new ArrayList<>(List.of()),
                new ArrayList<>(List.of())
        );

        shoppingListOutput = new ShoppingListOutput(
                listId,
                "Lista teste sem item",
                new ArrayList<>(List.of()),
                new ArrayList<>(List.of())
        );
    }

    @Test
    void shouldRemoveItemFromShoppingListSuccessfully() {
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = removeItemFromListService.removeItemFromShoppingList(listId, itemId);

        assertEquals(0, result.items().size());
    }

    @Test
    void shouldThrowExceptionWhenListNotFound() {
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> removeItemFromListService.removeItemFromShoppingList(listId, itemId));

        assertEquals("Shopping list not found", ex.getMessage());

        verify(shoppingListRepositoryPort, never()).save(any());
        verify(shoppingListMapper, never()).toOutput(any());
    }

    @Test
    void shouldThrowExceptionWhenItemNotFound() {
        shoppingList = new ShoppingList(
                listId,
                "Lista teste sem item",
                new ArrayList<>(List.of()),
                new ArrayList<>(List.of())
        );
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));

        RuntimeException ex = assertThrows(ItemNotFoundException.class,
                () -> removeItemFromListService.removeItemFromShoppingList(listId, itemId));

        assertEquals("Item not found", ex.getMessage());

        verify(shoppingListRepositoryPort, never()).save(any());
        verify(shoppingListMapper, never()).toOutput(any());
    }

}
