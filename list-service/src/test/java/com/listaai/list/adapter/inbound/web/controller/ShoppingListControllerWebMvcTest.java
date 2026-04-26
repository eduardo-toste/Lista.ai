package com.listaai.list.adapter.inbound.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.listaai.list.adapter.inbound.web.handler.ErrorResponseFactory;
import com.listaai.list.adapter.inbound.web.handler.GlobalExceptionHandler;
import com.listaai.list.adapter.inbound.web.mapper.ShoppingListItemWebMapper;
import com.listaai.list.adapter.inbound.web.mapper.ShoppingListParticipantWebMapper;
import com.listaai.list.adapter.inbound.web.mapper.ShoppingListWebMapper;
import com.listaai.list.adapter.inbound.web.request.CreateShoppingListItemRequest;
import com.listaai.list.adapter.inbound.web.request.CreateShoppingListParticipantRequest;
import com.listaai.list.adapter.inbound.web.request.CreateShoppingListRequest;
import com.listaai.list.application.dto.output.ShoppingListItemOutput;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.dto.output.ShoppingListParticipantOutput;
import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.application.port.inbound.lists.CreateShoppingListUseCase;
import com.listaai.list.application.port.inbound.lists.DeleteShoppingListUseCase;
import com.listaai.list.application.port.inbound.lists.GetShoppingListUseCase;
import com.listaai.list.application.port.inbound.lists.UpdateListNameUseCase;
import com.listaai.list.domain.enums.ItemUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ShoppingListController.class)
@Import({
        ShoppingListWebMapper.class,
        ShoppingListItemWebMapper.class,
        ShoppingListParticipantWebMapper.class,
        GlobalExceptionHandler.class,
        ErrorResponseFactory.class
})
class ShoppingListControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateShoppingListUseCase createShoppingListUseCase;

    @MockitoBean
    private GetShoppingListUseCase getShoppingListUseCase;

    @MockitoBean
    private UpdateListNameUseCase updateListNameUseCase;

    @MockitoBean
    private DeleteShoppingListUseCase deleteShoppingListUseCase;

    @Test
    void shouldCreateShoppingList() throws Exception {
        ShoppingListOutput output = new ShoppingListOutput(
                1L,
                "Churrasco",
                List.of(new ShoppingListItemOutput(10L, "Carvao", 2, ItemUnit.UN, false)),
                List.of(new ShoppingListParticipantOutput(20L, "Eduardo", "11999999999"))
        );

        when(createShoppingListUseCase.createShoppingList(any())).thenReturn(output);

        CreateShoppingListRequest request = new CreateShoppingListRequest(
                "Churrasco",
                List.of(new CreateShoppingListItemRequest("Carvao", 2, ItemUnit.UN)),
                List.of(new CreateShoppingListParticipantRequest("Eduardo", "11999999999"))
        );

        mockMvc.perform(post("/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Churrasco"))
                .andExpect(jsonPath("$.items[0].id").value(10))
                .andExpect(jsonPath("$.items[0].name").value("Carvao"))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.items[0].unit").value("UN"))
                .andExpect(jsonPath("$.items[0].purchased").value(false))
                .andExpect(jsonPath("$.participants[0].id").value(20))
                .andExpect(jsonPath("$.participants[0].name").value("Eduardo"))
                .andExpect(jsonPath("$.participants[0].phoneNumber").value("11999999999"));

        verify(createShoppingListUseCase).createShoppingList(any());
    }

    @Test
    void shouldReturnBadRequestWhenShoppingListCreationRequestIsInvalid() throws Exception {
        CreateShoppingListRequest request = new CreateShoppingListRequest(
                null,
                List.of(new CreateShoppingListItemRequest("Carvao", 2,
                        ItemUnit.UN)),
                List.of(new CreateShoppingListParticipantRequest("Eduardo",
                        "11999999999"))
        );

        mockMvc.perform(post("/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("name: Shopping list name must not be blank"))
                .andExpect(jsonPath("$.path").value("/lists"));

        verify(createShoppingListUseCase, never()).createShoppingList(any());
    }

    @Test
    void shouldReturnBadRequestWhenItemQuantityIsInvalid() throws Exception {
        CreateShoppingListRequest request = new CreateShoppingListRequest(
                "Churrasco",
                List.of(new CreateShoppingListItemRequest("Carvao", 0, ItemUnit.UN)),
                List.of(new CreateShoppingListParticipantRequest("Eduardo", "11999999999"))
        );

        mockMvc.perform(post("/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("items[0].quantity: Item quantity must be at least 1"))
                .andExpect(jsonPath("$.path").value("/lists"));

        verify(createShoppingListUseCase, never()).createShoppingList(any());
    }

    @Test
    void shouldReturnBadRequestWhenParticipantPhoneNumberIsInvalid() throws Exception {
        CreateShoppingListRequest request = new CreateShoppingListRequest(
                "Churrasco",
                List.of(new CreateShoppingListItemRequest("Carvao", 1, ItemUnit.UN)),
                List.of(new CreateShoppingListParticipantRequest("Eduardo", null))
        );

        mockMvc.perform(post("/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("participants[0].phoneNumber: Participant phone number must not be blank"))
                .andExpect(jsonPath("$.path").value("/lists"));

        verify(createShoppingListUseCase, never()).createShoppingList(any());
    }

    @Test
    void shouldReturnBadRequestWhenShoppingListCreationRequestBodyIsMalformed() throws Exception {
        String malformedJson = """
                {
                  "name": "Churrasco",
                  "items": [
                    {
                      "name": "Carvao",
                      "quantity": 2,
                      "unit": "UN"
                    }
                  ],
                  "participants": [
                    {
                      "name": "Eduardo",
                      "phoneNumber": "11999999999"
                    }
                  ]
                """;

        mockMvc.perform(post("/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Malformed or unreadable JSON request body"))
                .andExpect(jsonPath("$.path").value("/lists"));

        verify(createShoppingListUseCase, never()).createShoppingList(any());
    }

    @Test
    void shouldReturnShoppingListById() throws Exception {
        Long listId = 1L;
        ShoppingListOutput output = new ShoppingListOutput(
                listId,
                "Churrasco",
                List.of(new ShoppingListItemOutput(10L, "Carvao", 2, ItemUnit.UN, false)),
                List.of(new ShoppingListParticipantOutput(20L, "Eduardo", "11999999999"))
        );

        when(getShoppingListUseCase.getShoppingListById(listId)).thenReturn(output);

        mockMvc.perform(get("/lists/{id}", listId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Churrasco"))
                .andExpect(jsonPath("$.items[0].id").value(10))
                .andExpect(jsonPath("$.items[0].name").value("Carvao"))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.items[0].unit").value("UN"))
                .andExpect(jsonPath("$.items[0].purchased").value(false))
                .andExpect(jsonPath("$.participants[0].id").value(20))
                .andExpect(jsonPath("$.participants[0].name").value("Eduardo"))
                .andExpect(jsonPath("$.participants[0].phoneNumber").value("11999999999"));

        verify(getShoppingListUseCase).getShoppingListById(listId);
    }

    @Test
    void shouldReturnNotFoundWhenShoppingListDoesNotExist() throws Exception {
        Long listId = 999L;
        when(getShoppingListUseCase.getShoppingListById(listId))
                .thenThrow(new ShoppingListNotFoundException());

        mockMvc.perform(get("/lists/{id}", listId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Shopping list not found"))
                .andExpect(jsonPath("$.path").value("/lists/999"));

        verify(getShoppingListUseCase).getShoppingListById(listId);
    }

    @Test
    void shouldReturnPageOfShoppingLists() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        ShoppingListOutput output = new ShoppingListOutput(
                1L,
                "Churrasco",
                List.of(new ShoppingListItemOutput(10L, "Carvao", 2, ItemUnit.UN, false)),
                List.of(new ShoppingListParticipantOutput(20L, "Eduardo", "11999999999"))
        );
        Page<ShoppingListOutput> shoppingListsPage = new PageImpl<>(List.of(output), pageable, 1);

        when(getShoppingListUseCase.getShoppingLists(any(Pageable.class))).thenReturn(shoppingListsPage);

        mockMvc.perform(get("/lists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Churrasco"))
                .andExpect(jsonPath("$.content[0].items[0].id").value(10))
                .andExpect(jsonPath("$.content[0].items[0].name").value("Carvao"))
                .andExpect(jsonPath("$.content[0].items[0].quantity").value(2))
                .andExpect(jsonPath("$.content[0].items[0].unit").value("UN"))
                .andExpect(jsonPath("$.content[0].items[0].purchased").value(false))
                .andExpect(jsonPath("$.content[0].participants[0].id").value(20))
                .andExpect(jsonPath("$.content[0].participants[0].name").value("Eduardo"))
                .andExpect(jsonPath("$.content[0].participants[0].phoneNumber").value("11999999999"));
    }

    @Test
    void shouldReturnEmptyPageOfShoppingLists() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ShoppingListOutput> shoppingListsPage = new PageImpl<>(List.of(), pageable, 0);

        when(getShoppingListUseCase.getShoppingLists(any(Pageable.class))).thenReturn(shoppingListsPage);

        mockMvc.perform(get("/lists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }
}
