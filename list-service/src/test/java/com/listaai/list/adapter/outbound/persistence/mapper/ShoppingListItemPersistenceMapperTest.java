package com.listaai.list.adapter.outbound.persistence.mapper;

import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListItemEntity;
import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.model.ShoppingListItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShoppingListItemPersistenceMapperTest {

    private ShoppingListItemPersistenceMapper shoppingListItemPersistenceMapper;

    @BeforeEach
    void setUp() {
        shoppingListItemPersistenceMapper = new ShoppingListItemPersistenceMapper();
    }

    @Test
    void shouldMapItemDomainToEntity() {
        ShoppingListItem domain = new ShoppingListItem(1L, "Arroz", 2, ItemUnit.KG, false);

        ShoppingListItemEntity result = shoppingListItemPersistenceMapper.toEntity(domain);

        assertEquals(1L, result.getId());
        assertEquals("Arroz", result.getName());
        assertEquals(2, result.getQuantity());
        assertEquals(ItemUnit.KG, result.getUnit());
        assertFalse(result.isPurchased());
    }

    @Test
    void shouldMapItemEntityToDomain() {
        ShoppingListItemEntity entity = ShoppingListItemEntity.builder()
                .id(1L)
                .name("Feijao")
                .quantity(3)
                .unit(ItemUnit.UN)
                .purchased(true)
                .build();

        ShoppingListItem result = shoppingListItemPersistenceMapper.toDomain(entity);

        assertEquals(1L, result.getId());
        assertEquals("Feijao", result.getName());
        assertEquals(3, result.getQuantity());
        assertEquals(ItemUnit.UN, result.getUnit());
        assertTrue(result.isPurchased());
    }
}
