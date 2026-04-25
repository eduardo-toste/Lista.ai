package com.listaai.list.adapter.inbound.web.mapper;

import com.listaai.list.adapter.inbound.web.request.CreateShoppingListItemRequest;
import com.listaai.list.adapter.inbound.web.request.UpdateShoppingListItemRequest;
import com.listaai.list.adapter.inbound.web.response.ShoppingListItemResponse;
import com.listaai.list.application.dto.input.ShoppingListItemCommand;
import com.listaai.list.application.dto.output.ShoppingListItemOutput;
import com.listaai.list.domain.enums.ItemUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShoppingListItemWebMapperTest {

    private ShoppingListItemWebMapper shoppingListItemWebMapper;

    @BeforeEach
    void setUp() {
        shoppingListItemWebMapper = new ShoppingListItemWebMapper();
    }

    @Test
    void shouldMapCreateItemRequestToCommand() {
        CreateShoppingListItemRequest request = new CreateShoppingListItemRequest("Arroz", 2, ItemUnit.KG);

        ShoppingListItemCommand result = shoppingListItemWebMapper.toCommand(request);

        assertEquals("Arroz", result.name());
        assertEquals(2, result.quantity());
        assertEquals(ItemUnit.KG, result.unit());
    }

    @Test
    void shouldMapUpdateItemRequestToCommand() {
        UpdateShoppingListItemRequest request = new UpdateShoppingListItemRequest("Feijao", 1, ItemUnit.UN);

        ShoppingListItemCommand result = shoppingListItemWebMapper.toCommand(request);

        assertEquals("Feijao", result.name());
        assertEquals(1, result.quantity());
        assertEquals(ItemUnit.UN, result.unit());
    }

    @Test
    void shouldMapItemOutputToResponse() {
        ShoppingListItemOutput output = new ShoppingListItemOutput(1L, "Macarrao", 3, ItemUnit.PACK, true);

        ShoppingListItemResponse result = shoppingListItemWebMapper.toResponse(output);

        assertEquals(1L, result.id());
        assertEquals("Macarrao", result.name());
        assertEquals(3, result.quantity());
        assertEquals(ItemUnit.PACK, result.unit());
        assertTrue(result.purchased());

        ShoppingListItemOutput notPurchasedOutput = new ShoppingListItemOutput(2L, "Leite", 1, ItemUnit.L, false);
        ShoppingListItemResponse notPurchasedResult = shoppingListItemWebMapper.toResponse(notPurchasedOutput);
        assertFalse(notPurchasedResult.purchased());
    }
}
