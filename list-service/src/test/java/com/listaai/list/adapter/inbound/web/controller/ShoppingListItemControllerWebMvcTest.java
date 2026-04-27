package com.listaai.list.adapter.inbound.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.listaai.list.adapter.inbound.web.handler.ErrorResponseFactory;
import com.listaai.list.adapter.inbound.web.handler.GlobalExceptionHandler;
import com.listaai.list.adapter.inbound.web.mapper.ShoppingListItemWebMapper;
import com.listaai.list.adapter.inbound.web.mapper.ShoppingListParticipantWebMapper;
import com.listaai.list.adapter.inbound.web.mapper.ShoppingListWebMapper;
import com.listaai.list.adapter.inbound.web.request.CreateShoppingListItemRequest;
import com.listaai.list.adapter.inbound.web.request.PurchaseItemRequest;
import com.listaai.list.adapter.inbound.web.request.UpdateShoppingListItemRequest;
import com.listaai.list.application.dto.output.ShoppingListItemOutput;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.dto.output.ShoppingListParticipantOutput;
import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.application.port.inbound.items.AddItemToListUseCase;
import com.listaai.list.application.port.inbound.items.PurchaseItemFromListUseCase;
import com.listaai.list.application.port.inbound.items.RemoveItemFromListUseCase;
import com.listaai.list.application.port.inbound.items.UpdateItemFromListUseCase;
import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.exception.item.ItemAlreadyAddedException;
import com.listaai.list.domain.exception.item.ItemNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ShoppingListItemController.class)
@Import({
        ShoppingListWebMapper.class,
        ShoppingListItemWebMapper.class,
        ShoppingListParticipantWebMapper.class,
        GlobalExceptionHandler.class,
        ErrorResponseFactory.class
})
class ShoppingListItemControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AddItemToListUseCase addItemToListUseCase;

    @MockitoBean
    private RemoveItemFromListUseCase removeItemFromListUseCase;

    @MockitoBean
    private UpdateItemFromListUseCase updateItemFromListUseCase;

    @MockitoBean
    private PurchaseItemFromListUseCase purchaseItemFromListUseCase;

    @Test
    void shouldAddItemToShoppingList() throws Exception {
        Long listId = 1L;
        when(addItemToListUseCase.addItemToShoppingList(eq(listId), any())).thenReturn(sampleOutput(false));

        CreateShoppingListItemRequest request = new CreateShoppingListItemRequest("Carvao", 2, ItemUnit.UN);

        mockMvc.perform(post("/lists/{listId}/items", listId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.items[0].name").value("Carvao"))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.items[0].unit").value("UN"))
                .andExpect(jsonPath("$.items[0].purchased").value(false));

        verify(addItemToListUseCase).addItemToShoppingList(eq(listId), any());
    }

    @Test
    void shouldReturnBadRequestWhenAddingInvalidItem() throws Exception {
        Long listId = 1L;
        CreateShoppingListItemRequest request = new CreateShoppingListItemRequest("Carvao", 0, ItemUnit.UN);

        mockMvc.perform(post("/lists/{listId}/items", listId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("quantity: Item quantity must be at least 1"))
                .andExpect(jsonPath("$.path").value("/lists/1/items"));

        verify(addItemToListUseCase, never()).addItemToShoppingList(anyLong(), any());
    }

    @Test
    void shouldReturnConflictWhenAddingExistingItem() throws Exception {
        Long listId = 1L;
        CreateShoppingListItemRequest request = new CreateShoppingListItemRequest("Carvao", 2, ItemUnit.UN);

        when(addItemToListUseCase.addItemToShoppingList(eq(listId), any()))
                .thenThrow(new ItemAlreadyAddedException());

        mockMvc.perform(post("/lists/{listId}/items", listId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Item already exists"))
                .andExpect(jsonPath("$.path").value("/lists/1/items"));

        verify(addItemToListUseCase).addItemToShoppingList(eq(listId), any());
    }

    @Test
    void shouldRemoveItemFromShoppingList() throws Exception {
        Long listId = 1L;
        Long itemId = 10L;
        when(removeItemFromListUseCase.removeItemFromShoppingList(listId, itemId)).thenReturn(sampleOutput(false));

        mockMvc.perform(delete("/lists/{listId}/items/{itemId}", listId, itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Churrasco"));

        verify(removeItemFromListUseCase).removeItemFromShoppingList(listId, itemId);
    }

    @Test
    void shouldReturnNotFoundWhenRemovingNonexistentItem() throws Exception {
        Long listId = 1L;
        Long itemId = 999L;
        when(removeItemFromListUseCase.removeItemFromShoppingList(listId, itemId))
                .thenThrow(new ItemNotFoundException());

        mockMvc.perform(delete("/lists/{listId}/items/{itemId}", listId, itemId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Item not found"))
                .andExpect(jsonPath("$.path").value("/lists/1/items/999"));

        verify(removeItemFromListUseCase).removeItemFromShoppingList(listId, itemId);
    }

    @Test
    void shouldUpdateItemFromShoppingList() throws Exception {
        Long listId = 1L;
        Long itemId = 10L;
        when(updateItemFromListUseCase.updateItem(eq(listId), eq(itemId), any())).thenReturn(sampleOutput(false));

        UpdateShoppingListItemRequest request = new UpdateShoppingListItemRequest("Carvao", 3, ItemUnit.UN);

        mockMvc.perform(patch("/lists/{listId}/items/{itemId}", listId, itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].name").value("Carvao"));

        verify(updateItemFromListUseCase).updateItem(eq(listId), eq(itemId), any());
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingItemWithInvalidQuantity() throws Exception {
        Long listId = 1L;
        Long itemId = 10L;
        UpdateShoppingListItemRequest request = new UpdateShoppingListItemRequest("Carvao", 0, ItemUnit.UN);

        mockMvc.perform(patch("/lists/{listId}/items/{itemId}", listId, itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("quantity: Quantity must not be less than 1"))
                .andExpect(jsonPath("$.path").value("/lists/1/items/10"));

        verify(updateItemFromListUseCase, never()).updateItem(anyLong(), anyLong(), any());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonexistentItem() throws Exception {
        Long listId = 1L;
        Long itemId = 999L;
        UpdateShoppingListItemRequest request = new UpdateShoppingListItemRequest("Carvao", 3, ItemUnit.UN);

        when(updateItemFromListUseCase.updateItem(eq(listId), eq(itemId), any()))
                .thenThrow(new ItemNotFoundException());

        mockMvc.perform(patch("/lists/{listId}/items/{itemId}", listId, itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Item not found"))
                .andExpect(jsonPath("$.path").value("/lists/1/items/999"));

        verify(updateItemFromListUseCase).updateItem(eq(listId), eq(itemId), any());
    }

    @Test
    void shouldPurchaseItemFromShoppingList() throws Exception {
        Long listId = 1L;
        Long itemId = 10L;
        when(purchaseItemFromListUseCase.purchaseItemFromList(listId, itemId, true)).thenReturn(sampleOutput(true));

        PurchaseItemRequest request = new PurchaseItemRequest(true);

        mockMvc.perform(patch("/lists/{listId}/items/{itemId}/purchase", listId, itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].purchased").value(true));

        verify(purchaseItemFromListUseCase).purchaseItemFromList(listId, itemId, true);
    }

    @Test
    void shouldReturnBadRequestWhenPurchaseStatusIsMissing() throws Exception {
        Long listId = 1L;
        Long itemId = 10L;
        String body = "{}";

        mockMvc.perform(patch("/lists/{listId}/items/{itemId}/purchase", listId, itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("purchased: Purchase status must not be null"))
                .andExpect(jsonPath("$.path").value("/lists/1/items/10/purchase"));

        verify(purchaseItemFromListUseCase, never()).purchaseItemFromList(anyLong(), anyLong(), any(Boolean.class));
    }

    @Test
    void shouldReturnNotFoundWhenPurchasingNonexistentItem() throws Exception {
        Long listId = 1L;
        Long itemId = 999L;
        PurchaseItemRequest request = new PurchaseItemRequest(true);

        when(purchaseItemFromListUseCase.purchaseItemFromList(listId, itemId, true))
                .thenThrow(new ItemNotFoundException());

        mockMvc.perform(patch("/lists/{listId}/items/{itemId}/purchase", listId, itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Item not found"))
                .andExpect(jsonPath("$.path").value("/lists/1/items/999/purchase"));

        verify(purchaseItemFromListUseCase).purchaseItemFromList(listId, itemId, true);
    }

    @Test
    void shouldReturnNotFoundWhenAddingItemToNonexistentShoppingList() throws Exception {
        Long listId = 999L;
        CreateShoppingListItemRequest request = new CreateShoppingListItemRequest("Carvao", 2, ItemUnit.UN);

        when(addItemToListUseCase.addItemToShoppingList(eq(listId), any()))
                .thenThrow(new ShoppingListNotFoundException());

        mockMvc.perform(post("/lists/{listId}/items", listId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Shopping list not found"))
                .andExpect(jsonPath("$.path").value("/lists/999/items"));

        verify(addItemToListUseCase).addItemToShoppingList(eq(listId), any());
    }

    private ShoppingListOutput sampleOutput(boolean purchased) {
        return new ShoppingListOutput(
                1L,
                "Churrasco",
                List.of(new ShoppingListItemOutput(10L, "Carvao", 2, ItemUnit.UN, purchased)),
                List.of(new ShoppingListParticipantOutput(20L, "Eduardo", "11999999999"))
        );
    }
}
