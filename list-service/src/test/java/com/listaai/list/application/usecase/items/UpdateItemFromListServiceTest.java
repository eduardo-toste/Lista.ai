package com.listaai.list.application.usecase.items;

import com.listaai.list.application.dto.input.ShoppingListItemCommand;
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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateItemFromListServiceTest {

    @Mock
    private ShoppingListRepositoryPort shoppingListRepositoryPort;

    @Mock
    private ShoppingListMapper shoppingListMapper;

    @InjectMocks
    private UpdateItemFromListService updateItemFromListService;

    private final Long listId = 1L;
    private final Long itemId = 1L;

    private ShoppingList shoppingList;
    private ShoppingList savedShoppingList;
    private ShoppingListOutput shoppingListOutput;
    private ShoppingListItemCommand command;

    @BeforeEach
    void setUp() {
        shoppingList = new ShoppingList(
                listId,
                "Lista teste com item",
                List.of(new ShoppingListItem(itemId, "Arroz", 2, ItemUnit.KG)),
                List.of()
        );
        savedShoppingList = new ShoppingList(
                listId,
                "Lista teste com item atualizado",
                List.of(new ShoppingListItem(itemId, "Feijao", 1, ItemUnit.UN)),
                List.of()
        );
        shoppingListOutput = new ShoppingListOutput(
                listId,
                "Lista teste com item atualizado",
                List.of(),
                List.of()
        );
        command = new ShoppingListItemCommand("Feijao", 1, ItemUnit.UN);
    }

    @Test
    void shouldUpdateItemAndPersistUpdatedList() {
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListRepositoryPort.save(shoppingList)).thenReturn(savedShoppingList);
        when(shoppingListMapper.toOutput(savedShoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = updateItemFromListService.updateItem(listId, itemId, command);

        assertSame(shoppingListOutput, result);
        assertEquals("Feijao", shoppingList.getItems().getFirst().getName());
        assertEquals(1, shoppingList.getItems().getFirst().getQuantity());
        assertEquals(ItemUnit.UN, shoppingList.getItems().getFirst().getUnit());

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
                () -> updateItemFromListService.updateItem(listId, itemId, command)
        );

        assertEquals("Shopping list not found", exception.getMessage());
        verify(shoppingListRepositoryPort, never()).save(shoppingList);
        verify(shoppingListMapper, never()).toOutput(savedShoppingList);
    }

    @Test
    void shouldThrowWhenItemDoesNotExist() {
        shoppingList = new ShoppingList(listId, "Lista teste sem item", List.of(), List.of());
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));

        ItemNotFoundException exception = assertThrows(
                ItemNotFoundException.class,
                () -> updateItemFromListService.updateItem(listId, itemId, command)
        );

        assertEquals("Item not found", exception.getMessage());
        verify(shoppingListRepositoryPort, never()).save(shoppingList);
        verify(shoppingListMapper, never()).toOutput(savedShoppingList);
    }

}
