package com.listaai.list.application.usecase.items;

import com.listaai.list.application.dto.output.ShoppingListItemOutput;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseItemFromListServiceTest {

    @Mock
    private ShoppingListRepositoryPort shoppingListRepositoryPort;

    @Mock
    private ShoppingListMapper shoppingListMapper;

    @InjectMocks
    private PurchaseItemFromListService purchaseItemFromListService;

    private Long listId = 1L;
    private Long itemId = 1L;

    private ShoppingList shoppingList;
    private ShoppingList savedShoppingList;
    private ShoppingListOutput shoppingListOutput;

    @BeforeEach
    void setUp() {
        shoppingList = new ShoppingList(
                listId,
                "Lista teste",
                new ArrayList<>(List.of(new ShoppingListItem(itemId, "Arroz", 2, ItemUnit.KG, false))),
                new ArrayList<>(List.of())
        );

        savedShoppingList = new ShoppingList(
                listId,
                "Lista teste",
                new ArrayList<>(List.of(new ShoppingListItem(itemId, "Arroz", 2, ItemUnit.KG, true))),
                new ArrayList<>(List.of())
        );

        shoppingListOutput = new ShoppingListOutput(
                listId,
                "Lista teste",
                new ArrayList<>(List.of(new ShoppingListItemOutput(itemId, "Arroz", 2, ItemUnit.KG, true))),
                new ArrayList<>(List.of())
        );
    }

    @Test
    void shouldMarkItemAsPurchasedSuccessfully() {
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = purchaseItemFromListService.purchaseItemFromList(listId, itemId, true);

        assertTrue(result.items().getFirst().purchased());
    }

    @Test
    void shouldUnmarkItemAsPurchasedSuccessfully() {
        ShoppingList purchasedShoppingList = new ShoppingList(
                listId,
                "Lista teste",
                new ArrayList<>(List.of(new ShoppingListItem(itemId, "Arroz", 2, ItemUnit.KG, true))),
                new ArrayList<>(List.of())
        );
        ShoppingList unmarkedSavedShoppingList = new ShoppingList(
                listId,
                "Lista teste",
                new ArrayList<>(List.of(new ShoppingListItem(itemId, "Arroz", 2, ItemUnit.KG, false))),
                new ArrayList<>(List.of())
        );
        ShoppingListOutput unmarkedOutput = new ShoppingListOutput(
                listId,
                "Lista teste",
                new ArrayList<>(List.of(new ShoppingListItemOutput(itemId, "Arroz", 2, ItemUnit.KG, false))),
                new ArrayList<>(List.of())
        );

        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(purchasedShoppingList));
        when(shoppingListRepositoryPort.save(purchasedShoppingList)).thenReturn(unmarkedSavedShoppingList);
        when(shoppingListMapper.toOutput(unmarkedSavedShoppingList)).thenReturn(unmarkedOutput);

        ShoppingListOutput result = purchaseItemFromListService.purchaseItemFromList(listId, itemId, false);

        assertFalse(result.items().getFirst().purchased());
    }

    @Test
    void shouldThrowExceptionWhenListNotFound() {
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> purchaseItemFromListService.purchaseItemFromList(listId, itemId, true));

        assertEquals("Shopping list not found", ex.getMessage());

        verify(shoppingListRepositoryPort, never()).save(any());
        verify(shoppingListMapper, never()).toOutput(any());
    }

    @Test
    void shouldThrowExceptionWhenItemNotFound() {
        shoppingList = new ShoppingList(
                listId,
                "Lista teste",
                new ArrayList<>(List.of()),
                new ArrayList<>(List.of())
        );
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));

        RuntimeException ex = assertThrows(ItemNotFoundException.class,
                () -> purchaseItemFromListService.purchaseItemFromList(listId, itemId, true));

        assertEquals("Item not found", ex.getMessage());

        verify(shoppingListRepositoryPort, never()).save(any());
        verify(shoppingListMapper, never()).toOutput(any());
    }

}
