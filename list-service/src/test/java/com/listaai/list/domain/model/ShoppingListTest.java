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

import static org.junit.jupiter.api.Assertions.*;

class ShoppingListTest {

    private ShoppingList shoppingList;

    @BeforeEach
    void setUp() {
        shoppingList = new ShoppingList(1L, "Lista teste", null, null);
    }

    @Test
    void shouldCreateShoppingListSuccessfullyWhenItemsAndParticipantsAreNull() {
        assertEquals(1L, shoppingList.getId());
        assertEquals("Lista teste", shoppingList.getName());
        assertEquals(List.of(), shoppingList.getItems());
        assertEquals(List.of(), shoppingList.getParticipants());
    }

    @Test
    void shouldCreateShoppingListSuccessfullyWhenItemsAndParticipantsExists() {
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                List.of(new ShoppingListItem(1L, "Item", 1, ItemUnit.UN, true)),
                List.of(new ShoppingListParticipant(1L, "Eduardo", "11999998888")));

        assertEquals(1L, shoppingList.getId());
        assertEquals("Lista teste", shoppingList.getName());
        assertEquals(1L, shoppingList.getItems().getFirst().getId());
        assertEquals("Item", shoppingList.getItems().getFirst().getName());
        assertEquals(1L, shoppingList.getParticipants().getFirst().getId());
        assertEquals("Eduardo", shoppingList.getParticipants().getFirst().getName());
    }

    @Test
    void shouldUpdateListNameSuccessfully() {
        shoppingList.updateListName("Novo nome de lista");
        assertEquals("Novo nome de lista", shoppingList.getName());
    }

    @Test
    void shouldAddItemToListSuccessfully() {
        ShoppingListItem item = new ShoppingListItem(1L, "Item", 1, ItemUnit.UN);
        shoppingList.addItem(item);

        assertEquals(1L, shoppingList.getItems().getFirst().getId());
        assertEquals("Item", shoppingList.getItems().getFirst().getName());
    }

    @Test
    void shouldNotAddItemToListWhenItemAlreadyExists() {
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                List.of(new ShoppingListItem(1L, "Item", 1, ItemUnit.UN, true)),
                null);
        ShoppingListItem item = new ShoppingListItem(1L, "Item", 1, ItemUnit.UN);

        RuntimeException ex = assertThrows(ItemAlreadyAddedException.class,
                () -> shoppingList.addItem(item));

        assertEquals("Item already exists", ex.getMessage());
    }

    @Test
    void shouldRemoveItemFromListSuccessfully() {
        ShoppingListItem item = new ShoppingListItem(1L, "Item", 1, ItemUnit.UN);
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(List.of(item)),
                new ArrayList<>());

        shoppingList.removeItem(1L);

        assertFalse(shoppingList.getItems().contains(item));
    }

    @Test
    void shouldNotRemoveItemFromListWhenItemDoNotExists() {
        RuntimeException ex = assertThrows(ItemNotFoundException.class,
                () -> shoppingList.removeItem(1L));

        assertEquals("Item not found", ex.getMessage());
    }

    @Test
    void shouldUpdateItemFromListSuccessfully() {
        ShoppingListItem item = new ShoppingListItem(1L, "Item", 1, ItemUnit.UN);
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(List.of(item)),
                new ArrayList<>());

        shoppingList.updateItem(1L, "Novo nome", 100, ItemUnit.G);

        assertEquals("Novo nome", shoppingList.getItems().getFirst().getName());
        assertEquals(100, shoppingList.getItems().getFirst().getQuantity());
        assertEquals(ItemUnit.G, shoppingList.getItems().getFirst().getUnit());
    }

    @Test
    void shouldNotUpdateItemFromListWhenItemDoNotExists() {
        RuntimeException ex = assertThrows(ItemNotFoundException.class,
                () -> shoppingList.updateItem(1L, "Novo nome", 100, ItemUnit.UN));

        assertEquals("Item not found", ex.getMessage());
    }

    @Test
    void shouldNotAddItemToListWhenItemAlreadyExistsWithDifferentCasing() {
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(List.of(new ShoppingListItem(1L, "Item", 1, ItemUnit.UN, false))),
                new ArrayList<>());
        ShoppingListItem item = new ShoppingListItem(2L, "ITEM", 1, ItemUnit.UN);

        RuntimeException ex = assertThrows(ItemAlreadyAddedException.class,
                () -> shoppingList.addItem(item));

        assertEquals("Item already exists", ex.getMessage());
    }

    @Test
    void shouldMarkItemAsPurchasedSuccessfully() {
        ShoppingListItem item = new ShoppingListItem(1L, "Item", 1, ItemUnit.UN, false);
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(List.of(item)),
                new ArrayList<>());

        shoppingList.markItemAsPurchased(1L);

        assertTrue(shoppingList.getItems().getFirst().isPurchased());
    }

    @Test
    void shouldNotMarkItemAsPurchasedWhenItemDoNotExists() {
        RuntimeException ex = assertThrows(ItemNotFoundException.class,
                () -> shoppingList.markItemAsPurchased(99L));

        assertEquals("Item not found", ex.getMessage());
    }

    @Test
    void shouldUnmarkItemAsPurchasedSuccessfully() {
        ShoppingListItem item = new ShoppingListItem(1L, "Item", 1, ItemUnit.UN, true);
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(List.of(item)),
                new ArrayList<>());

        shoppingList.unmarkItemAsPurchased(1L);

        assertFalse(shoppingList.getItems().getFirst().isPurchased());
    }

    @Test
    void shouldNotUnmarkItemAsPurchasedWhenItemDoNotExists() {
        RuntimeException ex = assertThrows(ItemNotFoundException.class,
                () -> shoppingList.unmarkItemAsPurchased(99L));

        assertEquals("Item not found", ex.getMessage());
    }

    @Test
    void shouldAddParticipantToListSuccessfully() {
        ShoppingListParticipant participant = new ShoppingListParticipant(1L, "Eduardo", "11999998888");
        shoppingList.addParticipant(participant);

        assertEquals(1, shoppingList.getParticipants().size());
        assertEquals("Eduardo", shoppingList.getParticipants().getFirst().getName());
        assertEquals("11999998888", shoppingList.getParticipants().getFirst().getPhoneNumber());
    }

    @Test
    void shouldNotAddParticipantToListWhenParticipantAlreadyExists() {
        ShoppingListParticipant participant = new ShoppingListParticipant(1L, "Eduardo", "11999998888");
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(),
                new ArrayList<>(List.of(participant)));

        RuntimeException ex = assertThrows(ParticipantAlreadyAddedException.class,
                () -> shoppingList.addParticipant(new ShoppingListParticipant(2L, "Outro", "11999998888")));

        assertEquals("Participant already exists", ex.getMessage());
    }

    @Test
    void shouldRemoveParticipantFromListSuccessfully() {
        ShoppingListParticipant participant = new ShoppingListParticipant(1L, "Eduardo", "11999998888");
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(),
                new ArrayList<>(List.of(participant)));

        shoppingList.removeParticipant(1L);

        assertFalse(shoppingList.getParticipants().contains(participant));
    }

    @Test
    void shouldNotRemoveParticipantFromListWhenParticipantDoNotExists() {
        RuntimeException ex = assertThrows(ParticipantNotFoundException.class,
                () -> shoppingList.removeParticipant(99L));

        assertEquals("Participant not found", ex.getMessage());
    }

    @Test
    void shouldUpdateParticipantFromListSuccessfully() {
        ShoppingListParticipant participant = new ShoppingListParticipant(1L, "Eduardo", "11999998888");
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(),
                new ArrayList<>(List.of(participant)));

        shoppingList.updateParticipant(1L, "Novo nome", "11988887777");

        assertEquals("Novo nome", shoppingList.getParticipants().getFirst().getName());
        assertEquals("11988887777", shoppingList.getParticipants().getFirst().getPhoneNumber());
    }

    @Test
    void shouldNotUpdateParticipantFromListWhenParticipantDoNotExists() {
        RuntimeException ex = assertThrows(ParticipantNotFoundException.class,
                () -> shoppingList.updateParticipant(99L, "Novo nome", "11988887777"));

        assertEquals("Participant not found", ex.getMessage());
    }

    @Test
    void shouldShareListSuccessfully() {
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(List.of(new ShoppingListItem(1L, "Item", 1, ItemUnit.UN))),
                new ArrayList<>(List.of(new ShoppingListParticipant(1L, "Eduardo", "11999998888"))));

        assertDoesNotThrow(() -> shoppingList.validateCanBeShared());
    }

    @Test
    void shouldNotShareListWhenItemsIsEmpty() {
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(),
                new ArrayList<>(List.of(new ShoppingListParticipant(1L, "Eduardo", "11999998888"))));

        RuntimeException ex = assertThrows(EmptyShoppingListCannotBeSharedException.class,
                () -> shoppingList.validateCanBeShared());

        assertEquals("Empty list can't be shared", ex.getMessage());
    }

    @Test
    void shouldNotShareListWhenParticipantsIsEmpty() {
        shoppingList = new ShoppingList(
                1L,
                "Lista teste",
                new ArrayList<>(List.of(new ShoppingListItem(1L, "Item", 1, ItemUnit.UN))),
                new ArrayList<>());

        RuntimeException ex = assertThrows(ShoppingListWithoutParticipantsCannotBeSharedException.class,
                () -> shoppingList.validateCanBeShared());

        assertEquals("List without participants can't be shared", ex.getMessage());
    }

}