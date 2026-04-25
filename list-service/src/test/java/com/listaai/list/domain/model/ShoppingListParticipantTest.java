package com.listaai.list.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShoppingListParticipantTest {

    private ShoppingListParticipant participant;

    @BeforeEach
    void setUp() {
        participant = new ShoppingListParticipant(1L, "Eduardo", "11999998888");
    }

    @Test
    void shouldCreateParticipantSuccessfully() {
        assertEquals(1L, participant.getId());
        assertEquals("Eduardo", participant.getName());
        assertEquals("11999998888", participant.getPhoneNumber());
    }

    @Test
    void shouldUpdateOnlyParticipantNameWhenPhoneNumberIsNull() {
        participant.update("Lucas", null);

        assertEquals("Lucas", participant.getName());
        assertEquals("11999998888", participant.getPhoneNumber());
    }

    @Test
    void shouldUpdateOnlyParticipantPhoneNumberWhenNameIsNull() {
        participant.update(null, "11111111111");

        assertEquals("Eduardo", participant.getName());
        assertEquals("11111111111", participant.getPhoneNumber());
    }

    @Test
    void shouldUpdateParticipantNameAndPhoneNumber() {
        participant.update("Lucas", "11111111111");

        assertEquals("Lucas", participant.getName());
        assertEquals("11111111111", participant.getPhoneNumber());
    }

    @Test
    void shouldKeepParticipantUnchangedWhenAllUpdateFieldsAreNull() {
        participant.update(null, null);

        assertEquals("Eduardo", participant.getName());
        assertEquals("11999998888", participant.getPhoneNumber());
    }

}
