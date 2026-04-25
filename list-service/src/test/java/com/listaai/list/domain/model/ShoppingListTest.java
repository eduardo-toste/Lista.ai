package com.listaai.list.domain.model;

import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.exception.item.ItemAlreadyAddedException;
import com.listaai.list.domain.exception.item.ItemNotFoundException;
import com.listaai.list.domain.exception.list.EmptyShoppingListCannotBeSharedException;
import com.listaai.list.domain.exception.list.ShoppingListWithoutParticipantsCannotBeSharedException;
import com.listaai.list.domain.exception.participant.ParticipantAlreadyAddedException;
import com.listaai.list.domain.exception.participant.ParticipantNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShoppingListTest {

    private ShoppingList shoppingList;

    @BeforeEach
    void setUp() {
        shoppingList = new ShoppingList(1L, "Lista teste", null, null);
    }

    @Test
    void shouldCreateShoppingListWithEmptyCollectionsWhenItemsAndParticipantsAreNull() {
        assertEquals(1L, shoppingList.getId());
        assertEquals("Lista teste", shoppingList.getName());
        assertEquals(List.of(), shoppingList.getItems());
        assertEquals(List.of(), shoppingList.getParticipants());
    }

    @Test
    void shouldCreateShoppingListWithProvidedItemsAndParticipants() {
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                List.of(new ShoppingListItem(1L, "Item", 1, ItemUnit.UN, true)),
                List.of(new ShoppingListParticipant(1L, "Eduardo", "11999998888"))
        );

        assertEquals(1L, shoppingList.getId());
        assertEquals("Lista teste", shoppingList.getName());
        assertEquals(1L, shoppingList.getItems().getFirst().getId());
        assertEquals("Item", shoppingList.getItems().getFirst().getName());
        assertEquals(1L, shoppingList.getParticipants().getFirst().getId());
        assertEquals("Eduardo", shoppingList.getParticipants().getFirst().getName());
    }

    @Test
    void shouldUpdateListName() {
        shoppingList.updateListName("Novo nome de lista");

        assertEquals("Novo nome de lista", shoppingList.getName());
    }

    @Test
    void shouldAddItemToList() {
        ShoppingListItem item = new ShoppingListItem(1L, "Item", 1, ItemUnit.UN);

        shoppingList.addItem(item);

        assertEquals(1, shoppingList.getItems().size());
        assertEquals(item, shoppingList.getItems().getFirst());
    }

    @Test
    void shouldThrowWhenAddingAnItemThatAlreadyExists() {
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                List.of(new ShoppingListItem(1L, "Item", 1, ItemUnit.UN, true)),
                null
        );
        ShoppingListItem item = new ShoppingListItem(1L, "Item", 1, ItemUnit.UN);

        ItemAlreadyAddedException exception = assertThrows(
                ItemAlreadyAddedException.class,
                () -> shoppingList.addItem(item)
        );

        assertEquals("Item already exists", exception.getMessage());
    }

    @Test
    void shouldThrowWhenAddingAnItemThatAlreadyExistsWithDifferentCasing() {
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(List.of(new ShoppingListItem(1L, "Item", 1, ItemUnit.UN, false))),
                new ArrayList<>()
        );
        ShoppingListItem item = new ShoppingListItem(2L, "ITEM", 1, ItemUnit.UN);

        ItemAlreadyAddedException exception = assertThrows(
                ItemAlreadyAddedException.class,
                () -> shoppingList.addItem(item)
        );

        assertEquals("Item already exists", exception.getMessage());
    }

    @Test
    void shouldRemoveItemFromList() {
        ShoppingListItem item = new ShoppingListItem(1L, "Item", 1, ItemUnit.UN);
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(List.of(item)),
                new ArrayList<>()
        );

        shoppingList.removeItem(1L);

        assertTrue(shoppingList.getItems().isEmpty());
        assertFalse(shoppingList.getItems().contains(item));
    }

    @Test
    void shouldThrowWhenRemovingAnItemThatDoesNotExist() {
        ItemNotFoundException exception = assertThrows(
                ItemNotFoundException.class,
                () -> shoppingList.removeItem(1L)
        );

        assertEquals("Item not found", exception.getMessage());
    }

    @Test
    void shouldUpdateItemFromList() {
        ShoppingListItem item = new ShoppingListItem(1L, "Item", 1, ItemUnit.UN);
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(List.of(item)),
                new ArrayList<>()
        );

        shoppingList.updateItem(1L, "Novo nome", 100, ItemUnit.G);

        assertEquals("Novo nome", shoppingList.getItems().getFirst().getName());
        assertEquals(100, shoppingList.getItems().getFirst().getQuantity());
        assertEquals(ItemUnit.G, shoppingList.getItems().getFirst().getUnit());
    }

    @Test
    void shouldThrowWhenUpdatingAnItemThatDoesNotExist() {
        ItemNotFoundException exception = assertThrows(
                ItemNotFoundException.class,
                () -> shoppingList.updateItem(1L, "Novo nome", 100, ItemUnit.UN)
        );

        assertEquals("Item not found", exception.getMessage());
    }

    @Test
    void shouldMarkItemAsPurchased() {
        ShoppingListItem item = new ShoppingListItem(1L, "Item", 1, ItemUnit.UN, false);
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(List.of(item)),
                new ArrayList<>()
        );

        shoppingList.markItemAsPurchased(1L);

        assertTrue(shoppingList.getItems().getFirst().isPurchased());
    }

    @Test
    void shouldThrowWhenMarkingAnItemAsPurchasedThatDoesNotExist() {
        ItemNotFoundException exception = assertThrows(
                ItemNotFoundException.class,
                () -> shoppingList.markItemAsPurchased(99L)
        );

        assertEquals("Item not found", exception.getMessage());
    }

    @Test
    void shouldUnmarkItemAsPurchased() {
        ShoppingListItem item = new ShoppingListItem(1L, "Item", 1, ItemUnit.UN, true);
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(List.of(item)),
                new ArrayList<>()
        );

        shoppingList.unmarkItemAsPurchased(1L);

        assertFalse(shoppingList.getItems().getFirst().isPurchased());
    }

    @Test
    void shouldThrowWhenUnmarkingAnItemAsPurchasedThatDoesNotExist() {
        ItemNotFoundException exception = assertThrows(
                ItemNotFoundException.class,
                () -> shoppingList.unmarkItemAsPurchased(99L)
        );

        assertEquals("Item not found", exception.getMessage());
    }

    @Test
    void shouldAddParticipantToList() {
        ShoppingListParticipant participant = new ShoppingListParticipant(1L, "Eduardo", "11999998888");

        shoppingList.addParticipant(participant);

        assertEquals(1, shoppingList.getParticipants().size());
        assertEquals(participant, shoppingList.getParticipants().getFirst());
    }

    @Test
    void shouldThrowWhenAddingAParticipantThatAlreadyExists() {
        ShoppingListParticipant participant = new ShoppingListParticipant(1L, "Eduardo", "11999998888");
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(),
                new ArrayList<>(List.of(participant))
        );

        ParticipantAlreadyAddedException exception = assertThrows(
                ParticipantAlreadyAddedException.class,
                () -> shoppingList.addParticipant(new ShoppingListParticipant(2L, "Outro", "11999998888"))
        );

        assertEquals("Participant already exists", exception.getMessage());
    }

    @Test
    void shouldRemoveParticipantFromList() {
        ShoppingListParticipant participant = new ShoppingListParticipant(1L, "Eduardo", "11999998888");
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(),
                new ArrayList<>(List.of(participant))
        );

        shoppingList.removeParticipant(1L);

        assertTrue(shoppingList.getParticipants().isEmpty());
        assertFalse(shoppingList.getParticipants().contains(participant));
    }

    @Test
    void shouldThrowWhenRemovingAParticipantThatDoesNotExist() {
        ParticipantNotFoundException exception = assertThrows(
                ParticipantNotFoundException.class,
                () -> shoppingList.removeParticipant(99L)
        );

        assertEquals("Participant not found", exception.getMessage());
    }

    @Test
    void shouldUpdateParticipantFromList() {
        ShoppingListParticipant participant = new ShoppingListParticipant(1L, "Eduardo", "11999998888");
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(),
                new ArrayList<>(List.of(participant))
        );

        shoppingList.updateParticipant(1L, "Novo nome", "11988887777");

        assertEquals("Novo nome", shoppingList.getParticipants().getFirst().getName());
        assertEquals("11988887777", shoppingList.getParticipants().getFirst().getPhoneNumber());
    }

    @Test
    void shouldThrowWhenUpdatingAParticipantThatDoesNotExist() {
        ParticipantNotFoundException exception = assertThrows(
                ParticipantNotFoundException.class,
                () -> shoppingList.updateParticipant(99L, "Novo nome", "11988887777")
        );

        assertEquals("Participant not found", exception.getMessage());
    }

    @Test
    void shouldAllowListToBeSharedWhenItHasItemsAndParticipants() {
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(List.of(new ShoppingListItem(1L, "Item", 1, ItemUnit.UN))),
                new ArrayList<>(List.of(new ShoppingListParticipant(1L, "Eduardo", "11999998888")))
        );

        assertDoesNotThrow(() -> shoppingList.validateCanBeShared());
    }

    @Test
    void shouldThrowWhenSharingAListWithoutItems() {
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(),
                new ArrayList<>(List.of(new ShoppingListParticipant(1L, "Eduardo", "11999998888")))
        );

        EmptyShoppingListCannotBeSharedException exception = assertThrows(
                EmptyShoppingListCannotBeSharedException.class,
                shoppingList::validateCanBeShared
        );

        assertEquals("Empty list can't be shared", exception.getMessage());
    }

    @Test
    void shouldThrowWhenSharingAListWithoutParticipants() {
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(List.of(new ShoppingListItem(1L, "Item", 1, ItemUnit.UN))),
                new ArrayList<>()
        );

        ShoppingListWithoutParticipantsCannotBeSharedException exception = assertThrows(
                ShoppingListWithoutParticipantsCannotBeSharedException.class,
                shoppingList::validateCanBeShared
        );

        assertEquals("List without participants can't be shared", exception.getMessage());
    }

}
