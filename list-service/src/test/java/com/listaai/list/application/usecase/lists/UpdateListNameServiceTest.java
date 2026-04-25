package com.listaai.list.application.usecase.lists;

import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.application.mapper.ShoppingListMapper;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.domain.model.ShoppingList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class UpdateListNameServiceTest {

    @Mock
    private ShoppingListRepositoryPort shoppingListRepositoryPort;

    @Mock
    private ShoppingListMapper shoppingListMapper;

    @InjectMocks
    private UpdateListNameService updateListNameService;

    private final Long listId = 1L;

    private ShoppingList shoppingList;
    private ShoppingList savedShoppingList;
    private ShoppingListOutput shoppingListOutput;

    @BeforeEach
    void setUp() {
        shoppingList = new ShoppingList(listId, "Lista antiga", List.of(), List.of());
        savedShoppingList = new ShoppingList(listId, "Lista nova", List.of(), List.of());
        shoppingListOutput = new ShoppingListOutput(listId, "Lista nova", List.of(), List.of());
    }

    @Test
    void shouldUpdateListNameAndPersistUpdatedList() {
        String newName = "Lista nova";
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = updateListNameService.updateName(listId, newName);

        assertSame(shoppingListOutput, result);
        assertEquals("Lista nova", shoppingList.getName());

        var inOrder = inOrder(shoppingListRepositoryPort, shoppingListMapper);
        inOrder.verify(shoppingListRepositoryPort).findById(listId);
        inOrder.verify(shoppingListRepositoryPort).save(shoppingList);
        inOrder.verify(shoppingListMapper).toOutput(savedShoppingList);
    }

    @Test
    void shouldThrowWhenListDoesNotExist() {
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.empty());

        ShoppingListNotFoundException exception = assertThrows(
                ShoppingListNotFoundException.class,
                () -> updateListNameService.updateName(listId, "Lista nova")
        );

        assertEquals("Shopping list not found", exception.getMessage());
        verify(shoppingListRepositoryPort, never()).save(shoppingList);
        verify(shoppingListMapper, never()).toOutput(savedShoppingList);
    }

}
