package com.listaai.list.adapter.inbound.web.mapper;

import com.listaai.list.adapter.inbound.web.request.CreateShoppingListParticipantRequest;
import com.listaai.list.adapter.inbound.web.request.UpdateShoppingListParticipantRequest;
import com.listaai.list.adapter.inbound.web.response.ShoppingListParticipantResponse;
import com.listaai.list.application.dto.input.ShoppingListParticipantCommand;
import com.listaai.list.application.dto.output.ShoppingListParticipantOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShoppingListParticipantWebMapperTest {

    private ShoppingListParticipantWebMapper shoppingListParticipantWebMapper;

    @BeforeEach
    void setUp() {
        shoppingListParticipantWebMapper = new ShoppingListParticipantWebMapper();
    }

    @Test
    void shouldMapCreateParticipantRequestToCommand() {
        CreateShoppingListParticipantRequest request = new CreateShoppingListParticipantRequest(
                "Eduardo",
                "11999990001"
        );

        ShoppingListParticipantCommand result = shoppingListParticipantWebMapper.toCommand(request);

        assertEquals("Eduardo", result.name());
        assertEquals("11999990001", result.phoneNumber());
    }

    @Test
    void shouldMapUpdateParticipantRequestToCommand() {
        UpdateShoppingListParticipantRequest request = new UpdateShoppingListParticipantRequest(
                "Maria",
                "11999990002"
        );

        ShoppingListParticipantCommand result = shoppingListParticipantWebMapper.toCommand(request);

        assertEquals("Maria", result.name());
        assertEquals("11999990002", result.phoneNumber());
    }

    @Test
    void shouldMapParticipantOutputToResponse() {
        ShoppingListParticipantOutput output = new ShoppingListParticipantOutput(
                1L,
                "Carlos",
                "11999990003"
        );

        ShoppingListParticipantResponse result = shoppingListParticipantWebMapper.toResponse(output);

        assertEquals(1L, result.id());
        assertEquals("Carlos", result.name());
        assertEquals("11999990003", result.phoneNumber());
    }
}
