package com.listaai.list.application.usecase.lists;

import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.domain.model.ShoppingList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteShoppingListServiceTest {

    @Mock
    private ShoppingListRepositoryPort shoppingListRepositoryPort;

    @InjectMocks
    private DeleteShoppingListService deleteShoppingListService;

    @Test
    void shouldDeleteShoppingListAfterConfirmingItExists() {
        Long listId = 1L;
        ShoppingList shoppingList = new ShoppingList(listId, "Lista teste", List.of(), List.of());
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));

        deleteShoppingListService.delete(listId);

        var inOrder = inOrder(shoppingListRepositoryPort);
        inOrder.verify(shoppingListRepositoryPort).findById(listId);
        inOrder.verify(shoppingListRepositoryPort).deleteById(listId);
    }

    @Test
    void shouldThrowWhenListDoesNotExist() {
        Long listId = 1L;
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.empty());

        ShoppingListNotFoundException exception = assertThrows(
                ShoppingListNotFoundException.class,
                () -> deleteShoppingListService.delete(listId)
        );

        assertEquals("Shopping list not found", exception.getMessage());
        verify(shoppingListRepositoryPort, never()).deleteById(listId);
    }

}
