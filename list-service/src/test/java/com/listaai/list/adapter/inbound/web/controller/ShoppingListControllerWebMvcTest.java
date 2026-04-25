package com.listaai.list.adapter.inbound.web.controller;

import com.listaai.list.adapter.inbound.web.handler.ErrorResponseFactory;
import com.listaai.list.adapter.inbound.web.handler.GlobalExceptionHandler;
import com.listaai.list.adapter.inbound.web.mapper.ShoppingListItemWebMapper;
import com.listaai.list.adapter.inbound.web.mapper.ShoppingListParticipantWebMapper;
import com.listaai.list.adapter.inbound.web.mapper.ShoppingListWebMapper;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @MockitoBean
    private CreateShoppingListUseCase createShoppingListUseCase;

    @MockitoBean
    private GetShoppingListUseCase getShoppingListUseCase;

    @MockitoBean
    private UpdateListNameUseCase updateListNameUseCase;

    @MockitoBean
    private DeleteShoppingListUseCase deleteShoppingListUseCase;

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
}
