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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetShoppingListServiceTest {

    @Mock
    private ShoppingListRepositoryPort shoppingListRepositoryPort;

    @Mock
    private ShoppingListMapper shoppingListMapper;

    @InjectMocks
    private GetShoppingListService getShoppingListService;

    private ShoppingList shoppingList;
    private ShoppingListOutput shoppingListOutput;

    @BeforeEach
    void setUp() {
        shoppingList = new ShoppingList(1L, "Lista teste", List.of(), List.of());
        shoppingListOutput = new ShoppingListOutput(1L, "Lista teste", List.of(), List.of());
    }

    @Test
    void shouldGetShoppingListById() {
        Long listId = 1L;
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.of(shoppingList));
        when(shoppingListMapper.toOutput(shoppingList)).thenReturn(shoppingListOutput);

        ShoppingListOutput result = getShoppingListService.getShoppingListById(listId);

        assertSame(shoppingListOutput, result);

        var inOrder = inOrder(shoppingListRepositoryPort, shoppingListMapper);
        inOrder.verify(shoppingListRepositoryPort).findById(listId);
        inOrder.verify(shoppingListMapper).toOutput(shoppingList);
    }

    @Test
    void shouldThrowWhenListDoesNotExist() {
        Long listId = 1L;
        when(shoppingListRepositoryPort.findById(listId)).thenReturn(Optional.empty());

        ShoppingListNotFoundException exception = assertThrows(
                ShoppingListNotFoundException.class,
                () -> getShoppingListService.getShoppingListById(listId)
        );

        assertEquals("Shopping list not found", exception.getMessage());
    }

    @Test
    void shouldGetShoppingListsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ShoppingList> shoppingLists = new PageImpl<>(List.of(shoppingList), pageable, 1);
        Page<ShoppingListOutput> shoppingListOutputs = new PageImpl<>(List.of(shoppingListOutput), pageable, 1);
        when(shoppingListRepositoryPort.findAll(pageable)).thenReturn(shoppingLists);
        when(shoppingListMapper.toPageOutput(shoppingLists)).thenReturn(shoppingListOutputs);

        Page<ShoppingListOutput> result = getShoppingListService.getShoppingLists(pageable);

        assertSame(shoppingListOutputs, result);

        var inOrder = inOrder(shoppingListRepositoryPort, shoppingListMapper);
        inOrder.verify(shoppingListRepositoryPort).findAll(pageable);
        inOrder.verify(shoppingListMapper).toPageOutput(shoppingLists);
    }

}
