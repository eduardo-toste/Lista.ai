package com.listaai.list.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    void shouldUpdateParticipantNameSuccessfully() {
        participant.update("Lucas", null);

        assertEquals("Lucas", participant.getName());
    }

    @Test
    void shouldUpdateParticipantPhoneNumberSuccessfully() {
        participant.update(null, "11111111111");

        assertEquals("11111111111", participant.getPhoneNumber());
    }

    @Test
    void shouldUpdateFullParticipantSuccessfully() {
        participant.update("Lucas", "11111111111");

        assertEquals("Lucas", participant.getName());
        assertEquals("11111111111", participant.getPhoneNumber());

    }

    @Test
    void shouldNotUpdateParticipantWhenBothFieldsAreNull() {
        participant.update(null, null);

        assertEquals("Eduardo", participant.getName());
        assertEquals("11999998888", participant.getPhoneNumber());
    }

}