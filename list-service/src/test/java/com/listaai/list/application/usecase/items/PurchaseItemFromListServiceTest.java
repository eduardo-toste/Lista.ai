package com.listaai.list.application.usecase.items;

import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.exception.ShoppingListNotFoundException;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.inOrder;
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

    private final Long listId = 1L;
    private final Long itemId = 1L;

    private ShoppingList shoppingList;
    private ShoppingList savedShoppingList;
    private ShoppingListOutput shoppingListOutput;

    @BeforeEach
    void setUp() {
        shoppingList = new ShoppingList(
                listId,
                "Lista teste",
                List.of(new ShoppingListItem(itemId, "Arroz", 2, ItemUnit.KG, false)),
                List.of()
        );
        savedShoppingList = new ShoppingList(
                listId,
                "Lista teste",
                List.of(new ShoppingListItem(itemId, "Arroz", 2, ItemUnit.KG, true)),
                List.of()
        );
        shoppingListOutput = new ShoppingListOutput(listId, "Lista teste", List.of(), List.of());
    }

    @Test
    void shouldMarkItemAsPurchasedAndPersistUpdatedList() {
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = purchaseItemFromListService.purchaseItemFromList(listId, itemId, true);

        assertSame(shoppingListOutput, result);
        assertTrue(shoppingList.getItems().getFirst().isPurchased());

        var inOrder = inOrder(shoppingListRepositoryPort, shoppingListMapper);
        inOrder.verify(shoppingListRepositoryPort).findById(listId);
        inOrder.verify(shoppingListRepositoryPort).save(shoppingList);
        inOrder.verify(shoppingListMapper).toOutput(savedShoppingList);
    }

    @Test
    void shouldUnmarkItemAsPurchasedAndPersistUpdatedList() {
        shoppingList = new ShoppingList(
                listId,
                "Lista teste",
                List.of(new ShoppingListItem(itemId, "Arroz", 2, ItemUnit.KG, true)),
                List.of()
        );
        savedShoppingList = new ShoppingList(
                listId,
                "Lista teste",
                List.of(new ShoppingListItem(itemId, "Arroz", 2, ItemUnit.KG, false)),
                List.of()
        );
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = purchaseItemFromListService.purchaseItemFromList(listId, itemId, false);

        assertSame(shoppingListOutput, result);
        assertFalse(shoppingList.getItems().getFirst().isPurchased());
    }

    @Test
    void shouldThrowWhenListDoesNotExist() {
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.empty());

        ShoppingListNotFoundException exception = assertThrows(
                ShoppingListNotFoundException.class,
                () -> purchaseItemFromListService.purchaseItemFromList(listId, itemId, true)
        );

        assertEquals("Shopping list not found", exception.getMessage());
        verify(shoppingListRepositoryPort, never()).save(shoppingList);
        verify(shoppingListMapper, never()).toOutput(savedShoppingList);
    }

    @Test
    void shouldThrowWhenItemDoesNotExist() {
        shoppingList = new ShoppingList(listId, "Lista teste", List.of(), List.of());
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));

        ItemNotFoundException exception = assertThrows(
                ItemNotFoundException.class,
                () -> purchaseItemFromListService.purchaseItemFromList(listId, itemId, true)
        );

        assertEquals("Item not found", exception.getMessage());
        verify(shoppingListRepositoryPort, never()).save(shoppingList);
        verify(shoppingListMapper, never()).toOutput(savedShoppingList);
    }

}
