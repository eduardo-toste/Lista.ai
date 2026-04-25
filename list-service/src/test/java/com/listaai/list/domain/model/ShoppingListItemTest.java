package com.listaai.list.domain.model;

import com.listaai.list.domain.enums.ItemUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShoppingListItemTest {

    private ShoppingListItem item;

    @BeforeEach
    void setUp() {
        item = new ShoppingListItem(1L, "Item", 1, ItemUnit.UN, true);
    }

    @Test
    void shouldCreateCompleteItemSuccessfully() {
        assertEquals(1L, item.getId());
        assertEquals("Item", item.getName());
        assertEquals(1, item.getQuantity());
        assertEquals(ItemUnit.UN, item.getUnit());
        assertTrue(item.isPurchased());
    }

    @Test
    void shouldCreateItemWithPurchasedDefaultingToFalse() {
        ShoppingListItem result = new ShoppingListItem(1L, "Item", 1, ItemUnit.UN);

        assertEquals(1L, result.getId());
        assertEquals("Item", result.getName());
        assertEquals(1, result.getQuantity());
        assertEquals(ItemUnit.UN, result.getUnit());
        assertFalse(result.isPurchased());
    }

    @Test
    void shouldUpdateOnlyItemNameWhenOtherFieldsAreNull() {
        item.update("New Item", null, null);

        assertEquals("New Item", item.getName());
        assertEquals(1, item.getQuantity());
        assertEquals(ItemUnit.UN, item.getUnit());
        assertTrue(item.isPurchased());
    }

    @Test
    void shouldUpdateOnlyItemQuantityWhenOtherFieldsAreNull() {
        item.update(null, 5, null);

        assertEquals("Item", item.getName());
        assertEquals(5, item.getQuantity());
        assertEquals(ItemUnit.UN, item.getUnit());
        assertTrue(item.isPurchased());
    }

    @Test
    void shouldUpdateAllMutableFields() {
        item.update("New Item", 5, ItemUnit.KG);

        assertEquals("New Item", item.getName());
        assertEquals(5, item.getQuantity());
        assertEquals(ItemUnit.KG, item.getUnit());
        assertTrue(item.isPurchased());
    }

    @Test
    void shouldKeepItemUnchangedWhenAllUpdateFieldsAreNull() {
        item.update(null, null, null);

        assertEquals("Item", item.getName());
        assertEquals(1, item.getQuantity());
        assertEquals(ItemUnit.UN, item.getUnit());
        assertTrue(item.isPurchased());
    }

    @Test
    void shouldMarkItemAsPurchased() {
        item.unmarkAsPurchased();

        item.markAsPurchased();

        assertTrue(item.isPurchased());
    }

    @Test
    void shouldUnmarkItemAsPurchased() {
        item.unmarkAsPurchased();

        assertFalse(item.isPurchased());
    }

}
