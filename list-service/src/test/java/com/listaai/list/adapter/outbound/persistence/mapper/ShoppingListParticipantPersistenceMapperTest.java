package com.listaai.list.adapter.outbound.persistence.mapper;

import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListParticipantEntity;
import com.listaai.list.domain.model.ShoppingListParticipant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShoppingListParticipantPersistenceMapperTest {

    private ShoppingListParticipantPersistenceMapper shoppingListParticipantPersistenceMapper;

    @BeforeEach
    void setUp() {
        shoppingListParticipantPersistenceMapper = new ShoppingListParticipantPersistenceMapper();
    }

    @Test
    void shouldMapParticipantDomainToEntity() {
        ShoppingListParticipant domain = new ShoppingListParticipant(1L, "Eduardo", "11999990001");

        ShoppingListParticipantEntity result = shoppingListParticipantPersistenceMapper.toEntity(domain);

        assertEquals(1L, result.getId());
        assertEquals("Eduardo", result.getName());
        assertEquals("11999990001", result.getPhoneNumber());
    }

    @Test
    void shouldMapParticipantEntityToDomain() {
        ShoppingListParticipantEntity entity = ShoppingListParticipantEntity.builder()
                .id(2L)
                .name("Maria")
                .phoneNumber("11999990002")
                .build();

        ShoppingListParticipant result = shoppingListParticipantPersistenceMapper.toDomain(entity);

        assertEquals(2L, result.getId());
        assertEquals("Maria", result.getName());
        assertEquals("11999990002", result.getPhoneNumber());
    }
}
