package com.listaai.list.adapter.inbound.web.mapper;

import com.listaai.list.adapter.inbound.web.request.CreateShoppingListItemRequest;
import com.listaai.list.adapter.inbound.web.request.CreateShoppingListParticipantRequest;
import com.listaai.list.adapter.inbound.web.request.CreateShoppingListRequest;
import com.listaai.list.adapter.inbound.web.response.ShoppingListResponse;
import com.listaai.list.application.dto.input.ShoppingListCommand;
import com.listaai.list.application.dto.output.ShoppingListItemOutput;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.dto.output.ShoppingListParticipantOutput;
import com.listaai.list.domain.enums.ItemUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingListWebMapperTest {

    private ShoppingListWebMapper shoppingListWebMapper;

    @BeforeEach
    void setUp() {
        shoppingListWebMapper = new ShoppingListWebMapper(
                new ShoppingListItemWebMapper(),
                new ShoppingListParticipantWebMapper()
        );
    }

    @Test
    void shouldMapCreateShoppingListRequestToCommand() {
        CreateShoppingListRequest request = new CreateShoppingListRequest(
                "Lista do mercado",
                List.of(new CreateShoppingListItemRequest("Arroz", 2, ItemUnit.KG)),
                List.of(new CreateShoppingListParticipantRequest("Eduardo", "11999990001"))
        );

        ShoppingListCommand result = shoppingListWebMapper.toCommand(request);

        assertEquals("Lista do mercado", result.name());
        assertEquals(1, result.items().size());
        assertEquals("Arroz", result.items().getFirst().name());
        assertEquals(2, result.items().getFirst().quantity());
        assertEquals(ItemUnit.KG, result.items().getFirst().unit());
        assertEquals(1, result.participants().size());
        assertEquals("Eduardo", result.participants().getFirst().name());
        assertEquals("11999990001", result.participants().getFirst().phoneNumber());
    }

    @Test
    void shouldMapShoppingListOutputToResponse() {
        ShoppingListOutput output = new ShoppingListOutput(
                1L,
                "Lista do mercado",
                List.of(new ShoppingListItemOutput(10L, "Arroz", 2, ItemUnit.KG, false)),
                List.of(new ShoppingListParticipantOutput(20L, "Eduardo", "11999990001"))
        );

        ShoppingListResponse result = shoppingListWebMapper.toResponse(output);

        assertEquals(1L, result.id());
        assertEquals("Lista do mercado", result.name());
        assertEquals(1, result.items().size());
        assertEquals(10L, result.items().getFirst().id());
        assertEquals("Arroz", result.items().getFirst().name());
        assertEquals(2, result.items().getFirst().quantity());
        assertEquals(ItemUnit.KG, result.items().getFirst().unit());
        assertFalse(result.items().getFirst().purchased());
        assertEquals(1, result.participants().size());
        assertEquals(20L, result.participants().getFirst().id());
        assertEquals("Eduardo", result.participants().getFirst().name());
        assertEquals("11999990001", result.participants().getFirst().phoneNumber());
    }

    @Test
    void shouldMapShoppingListPageToResponsePage() {
        ShoppingListOutput firstList = new ShoppingListOutput(
                1L,
                "Lista 1",
                List.of(new ShoppingListItemOutput(10L, "Arroz", 2, ItemUnit.KG, false)),
                List.of(new ShoppingListParticipantOutput(20L, "Eduardo", "11999990001"))
        );
        ShoppingListOutput secondList = new ShoppingListOutput(
                2L,
                "Lista 2",
                List.of(new ShoppingListItemOutput(11L, "Feijao", 1, ItemUnit.UN, true)),
                List.of(new ShoppingListParticipantOutput(21L, "Maria", "11999990002"))
        );
        Page<ShoppingListOutput> page = new PageImpl<>(
                List.of(firstList, secondList),
                PageRequest.of(0, 2),
                2
        );

        Page<ShoppingListResponse> result = shoppingListWebMapper.toPageResponse(page);

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Lista 1", result.getContent().get(0).name());
        assertEquals("Arroz", result.getContent().get(0).items().getFirst().name());
        assertEquals("Lista 2", result.getContent().get(1).name());
        assertTrue(result.getContent().get(1).items().getFirst().purchased());
        assertEquals("Maria", result.getContent().get(1).participants().getFirst().name());
    }
}
