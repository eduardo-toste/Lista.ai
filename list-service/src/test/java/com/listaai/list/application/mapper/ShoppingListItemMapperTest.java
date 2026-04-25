package com.listaai.list.application.mapper;

import com.listaai.list.application.dto.input.ShoppingListItemCommand;
import com.listaai.list.application.dto.output.ShoppingListItemOutput;
import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.model.ShoppingListItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShoppingListItemMapperTest {

    private ShoppingListItemMapper shoppingListItemMapper;

    @BeforeEach
    void setUp() {
        shoppingListItemMapper = new ShoppingListItemMapper();
    }

    @Test
    void shouldMapItemCommandToDomain() {
        ShoppingListItemCommand command = new ShoppingListItemCommand("Arroz", 2, ItemUnit.KG);

        ShoppingListItem result = shoppingListItemMapper.toDomain(command);

        assertNull(result.getId());
        assertEquals("Arroz", result.getName());
        assertEquals(2, result.getQuantity());
        assertEquals(ItemUnit.KG, result.getUnit());
        assertFalse(result.isPurchased());
    }

    @Test
    void shouldMapItemDomainToOutput() {
        ShoppingListItem domain = new ShoppingListItem(1L, "Arroz", 2, ItemUnit.KG, true);

        ShoppingListItemOutput result = shoppingListItemMapper.toOutput(domain);

        assertEquals(1L, result.id());
        assertEquals("Arroz", result.name());
        assertEquals(2, result.quantity());
        assertEquals(ItemUnit.KG, result.unit());
        assertTrue(result.purchased());
    }

}
