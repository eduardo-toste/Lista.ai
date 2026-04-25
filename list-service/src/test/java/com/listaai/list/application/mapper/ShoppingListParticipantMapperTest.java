package com.listaai.list.application.mapper;

import com.listaai.list.application.dto.input.ShoppingListParticipantCommand;
import com.listaai.list.application.dto.output.ShoppingListParticipantOutput;
import com.listaai.list.domain.model.ShoppingListParticipant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ShoppingListParticipantMapperTest {

    private ShoppingListParticipantMapper shoppingListParticipantMapper;

    @BeforeEach
    void setUp() {
        shoppingListParticipantMapper = new ShoppingListParticipantMapper();
    }

    @Test
    void shouldMapParticipantCommandToDomain() {
        ShoppingListParticipantCommand command = new ShoppingListParticipantCommand(
                "Participante teste",
                "11999990001"
        );

        ShoppingListParticipant result = shoppingListParticipantMapper.toDomain(command);

        assertNull(result.getId());
        assertEquals("Participante teste", result.getName());
        assertEquals("11999990001", result.getPhoneNumber());
    }

    @Test
    void shouldMapParticipantDomainToOutput() {
        ShoppingListParticipant domain = new ShoppingListParticipant(
                1L,
                "Participante teste",
                "11999990001"
        );

        ShoppingListParticipantOutput result = shoppingListParticipantMapper.toOutput(domain);

        assertEquals(1L, result.id());
        assertEquals("Participante teste", result.name());
        assertEquals("11999990001", result.phoneNumber());
    }

}
