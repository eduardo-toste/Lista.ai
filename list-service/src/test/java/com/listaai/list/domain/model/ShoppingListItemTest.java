package com.listaai.list.domain.model;

import com.listaai.list.domain.enums.ItemUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(true, item.isPurchased());
    }

    @Test
    void shouldCreateItemWithoutPurchasedFieldSuccessfully(){
        ShoppingListItem item = new ShoppingListItem(1L, "Item", 1, ItemUnit.UN);

        assertEquals(1L, item.getId());
        assertEquals("Item", item.getName());
        assertEquals(1, item.getQuantity());
        assertEquals(ItemUnit.UN, item.getUnit());
        assertEquals(false, item.isPurchased());
    }

    @Test
    void shouldUpdateItemNameSuccessfully() {
        item.update("New Item", null, null);
        assertEquals("New Item", item.getName());
        assertEquals(1, item.getQuantity());
        assertEquals(ItemUnit.UN, item.getUnit());
    }

    @Test
    void shouldUpdateItemQuantitySuccessfully() {
        item.update(null, 5, null);
        assertEquals("Item", item.getName());
        assertEquals(5, item.getQuantity());
    }

    @Test
    void shouldUpdateAllFieldsSuccessfully() {
        item.update("New Item", 5, ItemUnit.KG);
        assertEquals("New Item", item.getName());
        assertEquals(5, item.getQuantity());
        assertEquals(ItemUnit.KG, item.getUnit());
    }

    @Test
    void shouldNotUpdateItemWhenAllFieldsAreNull() {
        item.update(null, null, null);
        assertEquals("Item", item.getName());
        assertEquals(1, item.getQuantity());
        assertEquals(ItemUnit.UN, item.getUnit());
    }

    @Test
    void shouldMarkItemAsPurchasedSuccesfully() {
        item.markAsPurchased();

        assertEquals(true, item.isPurchased());
    }

    @Test
    void shouldUnmarkItemAsPurchasedSuccesfully() {
        item.unmarkAsPurchased();

        assertEquals(false, item.isPurchased());
    }

}