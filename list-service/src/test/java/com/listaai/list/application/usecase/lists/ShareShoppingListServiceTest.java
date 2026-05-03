package com.listaai.list.application.usecase.lists;

import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.application.port.outbound.ShoppingListEventPublisherPort;
import com.listaai.list.application.port.outbound.ShoppingListRepositoryPort;
import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.exception.list.EmptyShoppingListCannotBeSharedException;
import com.listaai.list.domain.exception.list.ShoppingListWithoutParticipantsCannotBeSharedException;
import com.listaai.list.domain.model.ShoppingList;
import com.listaai.list.domain.model.ShoppingListItem;
import com.listaai.list.domain.model.ShoppingListParticipant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShareShoppingListServiceTest {

    @Mock
    private ShoppingListRepositoryPort shoppingListRepositoryPort;

    @Mock
    private ShoppingListEventPublisherPort shoppingListEventPublisherPort;

    @InjectMocks
    private ShareShoppingListService shareShoppingListService;

    @Test
    void shouldPublishShoppingListSharedEventWhenListIsValid() {
        ShoppingList shoppingList = new ShoppingList(
                1L,
                "Churrasco",
                List.of(new ShoppingListItem(1L, "Carvao", 2, ItemUnit.UN, false)),
                List.of(new ShoppingListParticipant(1L, "Eduardo", "11999999999"))
        );
        when(shoppingListRepositoryPort.findById(1L)).thenReturn(Optional.of(shoppingList));

        shareShoppingListService.shareShoppingList(1L);

        verify(shoppingListEventPublisherPort).publishShoppingListShared(shoppingList);
    }

    @Test
    void shouldThrowWhenShoppingListDoesNotExist() {
        when(shoppingListRepositoryPort.findById(1L)).thenReturn(Optional.empty());

        ShoppingListNotFoundException exception = assertThrows(
                ShoppingListNotFoundException.class,
                () -> shareShoppingListService.shareShoppingList(1L)
        );

        assertEquals("Shopping list not found", exception.getMessage());
        verify(shoppingListEventPublisherPort, never()).publishShoppingListShared(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldThrowWhenShoppingListHasNoItems() {
        ShoppingList shoppingList = new ShoppingList(
                1L,
                "Churrasco",
                List.of(),
                List.of(new ShoppingListParticipant(1L, "Eduardo", "11999999999"))
        );
        when(shoppingListRepositoryPort.findById(1L)).thenReturn(Optional.of(shoppingList));

        EmptyShoppingListCannotBeSharedException exception = assertThrows(
                EmptyShoppingListCannotBeSharedException.class,
                () -> shareShoppingListService.shareShoppingList(1L)
        );

        assertEquals("Empty list can't be shared", exception.getMessage());
        verify(shoppingListEventPublisherPort, never()).publishShoppingListShared(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldThrowWhenShoppingListHasNoParticipants() {
        ShoppingList shoppingList = new ShoppingList(
                1L,
                "Churrasco",
                List.of(new ShoppingListItem(1L, "Carvao", 2, ItemUnit.UN, false)),
                List.of()
        );
        when(shoppingListRepositoryPort.findById(1L)).thenReturn(Optional.of(shoppingList));

        ShoppingListWithoutParticipantsCannotBeSharedException exception = assertThrows(
                ShoppingListWithoutParticipantsCannotBeSharedException.class,
                () -> shareShoppingListService.shareShoppingList(1L)
        );

        assertEquals("List without participants can't be shared", exception.getMessage());
        verify(shoppingListEventPublisherPort, never()).publishShoppingListShared(org.mockito.ArgumentMatchers.any());
    }
}
