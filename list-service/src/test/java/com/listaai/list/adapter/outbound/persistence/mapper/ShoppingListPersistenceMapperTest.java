package com.listaai.list.adapter.outbound.persistence.mapper;

import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListEntity;
import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListItemEntity;
import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListParticipantEntity;
import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.model.ShoppingList;
import com.listaai.list.domain.model.ShoppingListItem;
import com.listaai.list.domain.model.ShoppingListParticipant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShoppingListPersistenceMapperTest {

    private ShoppingListPersistenceMapper shoppingListPersistenceMapper;

    @BeforeEach
    void setUp() {
        shoppingListPersistenceMapper = new ShoppingListPersistenceMapper(
                new ShoppingListItemPersistenceMapper(),
                new ShoppingListParticipantPersistenceMapper()
        );
    }

    @Test
    void shouldMapShoppingListDomainToEntity() {
        ShoppingList domain = new ShoppingList(
                1L,
                "Lista do mercado",
                List.of(new ShoppingListItem(10L, "Arroz", 2, ItemUnit.KG, false)),
                List.of(new ShoppingListParticipant(20L, "Eduardo", "11999990001"))
        );

        ShoppingListEntity result = shoppingListPersistenceMapper.toEntity(domain);

        assertEquals(1L, result.getId());
        assertEquals("Lista do mercado", result.getName());
        assertEquals(1, result.getItems().size());
        ShoppingListItemEntity itemEntity = result.getItems().iterator().next();
        assertEquals(10L, itemEntity.getId());
        assertEquals("Arroz", itemEntity.getName());
        assertEquals(2, itemEntity.getQuantity());
        assertEquals(ItemUnit.KG, itemEntity.getUnit());
        assertSame(result, itemEntity.getShoppingList());
        assertEquals(1, result.getParticipants().size());
        ShoppingListParticipantEntity participantEntity = result.getParticipants().iterator().next();
        assertEquals(20L, participantEntity.getId());
        assertEquals("Eduardo", participantEntity.getName());
        assertEquals("11999990001", participantEntity.getPhoneNumber());
    }

    @Test
    void shouldMapShoppingListEntityToDomain() {
        ShoppingListEntity entity = ShoppingListEntity.builder()
                .id(1L)
                .name("Lista do mercado")
                .items(new HashSet<>(Set.of(
                        ShoppingListItemEntity.builder()
                                .id(10L)
                                .name("Arroz")
                                .quantity(2)
                                .unit(ItemUnit.KG)
                                .purchased(true)
                                .build()
                )))
                .participants(new HashSet<>(Set.of(
                        ShoppingListParticipantEntity.builder()
                                .id(20L)
                                .name("Eduardo")
                                .phoneNumber("11999990001")
                                .build()
                )))
                .build();

        ShoppingList result = shoppingListPersistenceMapper.toDomain(entity);

        assertEquals(1L, result.getId());
        assertEquals("Lista do mercado", result.getName());
        assertEquals(1, result.getItems().size());
        assertEquals(10L, result.getItems().getFirst().getId());
        assertEquals("Arroz", result.getItems().getFirst().getName());
        assertEquals(2, result.getItems().getFirst().getQuantity());
        assertEquals(ItemUnit.KG, result.getItems().getFirst().getUnit());
        assertTrue(result.getItems().getFirst().isPurchased());
        assertEquals(1, result.getParticipants().size());
        assertEquals(20L, result.getParticipants().getFirst().getId());
        assertEquals("Eduardo", result.getParticipants().getFirst().getName());
        assertEquals("11999990001", result.getParticipants().getFirst().getPhoneNumber());
    }

    @Test
    void shouldMapShoppingListPageToDomainPage() {
        ShoppingListEntity firstEntity = ShoppingListEntity.builder()
                .id(1L)
                .name("Lista 1")
                .items(new HashSet<>(Set.of(
                        ShoppingListItemEntity.builder()
                                .id(10L)
                                .name("Arroz")
                                .quantity(2)
                                .unit(ItemUnit.KG)
                                .purchased(false)
                                .build()
                )))
                .participants(new HashSet<>(Set.of(
                        ShoppingListParticipantEntity.builder()
                                .id(20L)
                                .name("Eduardo")
                                .phoneNumber("11999990001")
                                .build()
                )))
                .build();
        ShoppingListEntity secondEntity = ShoppingListEntity.builder()
                .id(2L)
                .name("Lista 2")
                .items(new HashSet<>(Set.of(
                        ShoppingListItemEntity.builder()
                                .id(11L)
                                .name("Feijao")
                                .quantity(1)
                                .unit(ItemUnit.UN)
                                .purchased(true)
                                .build()
                )))
                .participants(new HashSet<>(Set.of(
                        ShoppingListParticipantEntity.builder()
                                .id(21L)
                                .name("Maria")
                                .phoneNumber("11999990002")
                                .build()
                )))
                .build();
        Page<ShoppingListEntity> page = new PageImpl<>(
                List.of(firstEntity, secondEntity),
                PageRequest.of(0, 2),
                2
        );

        Page<ShoppingList> result = shoppingListPersistenceMapper.toPageDomain(page);

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Lista 1", result.getContent().get(0).getName());
        assertNotNull(result.getContent().get(0).getItems());
        assertEquals("Arroz", result.getContent().get(0).getItems().getFirst().getName());
        assertEquals("Lista 2", result.getContent().get(1).getName());
        assertTrue(result.getContent().get(1).getItems().getFirst().isPurchased());
        assertEquals("Maria", result.getContent().get(1).getParticipants().getFirst().getName());
    }
}
