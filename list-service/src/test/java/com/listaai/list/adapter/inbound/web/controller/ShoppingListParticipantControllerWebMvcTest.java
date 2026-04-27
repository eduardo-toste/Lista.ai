package com.listaai.list.adapter.inbound.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.listaai.list.adapter.inbound.web.handler.ErrorResponseFactory;
import com.listaai.list.adapter.inbound.web.handler.GlobalExceptionHandler;
import com.listaai.list.adapter.inbound.web.mapper.ShoppingListItemWebMapper;
import com.listaai.list.adapter.inbound.web.mapper.ShoppingListParticipantWebMapper;
import com.listaai.list.adapter.inbound.web.mapper.ShoppingListWebMapper;
import com.listaai.list.adapter.inbound.web.request.CreateShoppingListParticipantRequest;
import com.listaai.list.adapter.inbound.web.request.UpdateShoppingListParticipantRequest;
import com.listaai.list.application.dto.output.ShoppingListItemOutput;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.dto.output.ShoppingListParticipantOutput;
import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.application.port.inbound.participants.AddParticipantToListUseCase;
import com.listaai.list.application.port.inbound.participants.RemoveParticipantFromListUseCase;
import com.listaai.list.application.port.inbound.participants.UpdateParticipantFromListUseCase;
import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.exception.participant.ParticipantAlreadyAddedException;
import com.listaai.list.domain.exception.participant.ParticipantNotFoundException;
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

@WebMvcTest(controllers = ShoppingListParticipantController.class)
@Import({
        ShoppingListWebMapper.class,
        ShoppingListItemWebMapper.class,
        ShoppingListParticipantWebMapper.class,
        GlobalExceptionHandler.class,
        ErrorResponseFactory.class
})
class ShoppingListParticipantControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AddParticipantToListUseCase addParticipantToListUseCase;

    @MockitoBean
    private UpdateParticipantFromListUseCase updateParticipantFromListUseCase;

    @MockitoBean
    private RemoveParticipantFromListUseCase removeParticipantFromListUseCase;

    @Test
    void shouldAddParticipantToShoppingList() throws Exception {
        Long listId = 1L;
        when(addParticipantToListUseCase.addParticipantToShoppingList(eq(listId), any())).thenReturn(sampleOutput());

        CreateShoppingListParticipantRequest request = new CreateShoppingListParticipantRequest("Eduardo", "11999999999");

        mockMvc.perform(post("/lists/{listId}/participants", listId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participants[0].name").value("Eduardo"))
                .andExpect(jsonPath("$.participants[0].phoneNumber").value("11999999999"));

        verify(addParticipantToListUseCase).addParticipantToShoppingList(eq(listId), any());
    }

    @Test
    void shouldReturnBadRequestWhenAddingParticipantWithInvalidPhoneNumber() throws Exception {
        Long listId = 1L;
        CreateShoppingListParticipantRequest request = new CreateShoppingListParticipantRequest("Eduardo", "abc");

        mockMvc.perform(post("/lists/{listId}/participants", listId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("phoneNumber: Phone number must contain only digits and have 10 or 11 characters"))
                .andExpect(jsonPath("$.path").value("/lists/1/participants"));

        verify(addParticipantToListUseCase, never()).addParticipantToShoppingList(anyLong(), any());
    }

    @Test
    void shouldReturnConflictWhenAddingExistingParticipant() throws Exception {
        Long listId = 1L;
        CreateShoppingListParticipantRequest request = new CreateShoppingListParticipantRequest("Eduardo", "11999999999");

        when(addParticipantToListUseCase.addParticipantToShoppingList(eq(listId), any()))
                .thenThrow(new ParticipantAlreadyAddedException());

        mockMvc.perform(post("/lists/{listId}/participants", listId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Participant already exists"))
                .andExpect(jsonPath("$.path").value("/lists/1/participants"));

        verify(addParticipantToListUseCase).addParticipantToShoppingList(eq(listId), any());
    }

    @Test
    void shouldReturnNotFoundWhenAddingParticipantToNonexistentShoppingList() throws Exception {
        Long listId = 999L;
        CreateShoppingListParticipantRequest request = new CreateShoppingListParticipantRequest("Eduardo", "11999999999");

        when(addParticipantToListUseCase.addParticipantToShoppingList(eq(listId), any()))
                .thenThrow(new ShoppingListNotFoundException());

        mockMvc.perform(post("/lists/{listId}/participants", listId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Shopping list not found"))
                .andExpect(jsonPath("$.path").value("/lists/999/participants"));

        verify(addParticipantToListUseCase).addParticipantToShoppingList(eq(listId), any());
    }

    @Test
    void shouldUpdateParticipantFromShoppingList() throws Exception {
        Long listId = 1L;
        Long participantId = 20L;
        when(updateParticipantFromListUseCase.updateParticipantFromShoppingListUseCase(eq(listId), eq(participantId), any()))
                .thenReturn(sampleOutput());

        UpdateShoppingListParticipantRequest request = new UpdateShoppingListParticipantRequest("Eduardo Silva", "11988887777");

        mockMvc.perform(patch("/lists/{listId}/participants/{participantId}", listId, participantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participants[0].name").value("Eduardo"))
                .andExpect(jsonPath("$.participants[0].phoneNumber").value("11999999999"));

        verify(updateParticipantFromListUseCase)
                .updateParticipantFromShoppingListUseCase(eq(listId), eq(participantId), any());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonexistentParticipant() throws Exception {
        Long listId = 1L;
        Long participantId = 999L;
        UpdateShoppingListParticipantRequest request = new UpdateShoppingListParticipantRequest("Eduardo Silva", "11988887777");

        when(updateParticipantFromListUseCase.updateParticipantFromShoppingListUseCase(eq(listId), eq(participantId), any()))
                .thenThrow(new ParticipantNotFoundException());

        mockMvc.perform(patch("/lists/{listId}/participants/{participantId}", listId, participantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Participant not found"))
                .andExpect(jsonPath("$.path").value("/lists/1/participants/999"));

        verify(updateParticipantFromListUseCase)
                .updateParticipantFromShoppingListUseCase(eq(listId), eq(participantId), any());
    }

    @Test
    void shouldRemoveParticipantFromShoppingList() throws Exception {
        Long listId = 1L;
        Long participantId = 20L;
        when(removeParticipantFromListUseCase.removeParticipantFromShoppingList(listId, participantId)).thenReturn(sampleOutput());

        mockMvc.perform(delete("/lists/{listId}/participants/{participantId}", listId, participantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Churrasco"));

        verify(removeParticipantFromListUseCase).removeParticipantFromShoppingList(listId, participantId);
    }

    @Test
    void shouldReturnNotFoundWhenRemovingNonexistentParticipant() throws Exception {
        Long listId = 1L;
        Long participantId = 999L;
        when(removeParticipantFromListUseCase.removeParticipantFromShoppingList(listId, participantId))
                .thenThrow(new ParticipantNotFoundException());

        mockMvc.perform(delete("/lists/{listId}/participants/{participantId}", listId, participantId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Participant not found"))
                .andExpect(jsonPath("$.path").value("/lists/1/participants/999"));

        verify(removeParticipantFromListUseCase).removeParticipantFromShoppingList(listId, participantId);
    }

    private ShoppingListOutput sampleOutput() {
        return new ShoppingListOutput(
                1L,
                "Churrasco",
                List.of(new ShoppingListItemOutput(10L, "Carvao", 2, ItemUnit.UN, false)),
                List.of(new ShoppingListParticipantOutput(20L, "Eduardo", "11999999999"))
        );
    }
}
