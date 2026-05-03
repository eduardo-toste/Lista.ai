package com.listaai.list.application.utils;

import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.domain.model.ShoppingList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingListUtilsTest {

    @Mock
    private ShoppingListRepositoryPort shoppingListRepositoryPort;

    @Test
    void shouldReturnShoppingListWhenFound() {
        ShoppingList shoppingList = new ShoppingList(1L, "Churrasco", List.of(), List.of());
        when(shoppingListRepositoryPort.findById(1L)).thenReturn(Optional.of(shoppingList));

        ShoppingList result = ShoppingListUtils.findListOrThrow(shoppingListRepositoryPort, 1L);

        assertSame(shoppingList, result);
    }

    @Test
    void shouldThrowWhenShoppingListIsMissing() {
        when(shoppingListRepositoryPort.findById(1L)).thenReturn(Optional.empty());

        ShoppingListNotFoundException exception = assertThrows(
                ShoppingListNotFoundException.class,
                () -> ShoppingListUtils.findListOrThrow(shoppingListRepositoryPort, 1L)
        );

        assertEquals("Shopping list not found", exception.getMessage());
    }
}
