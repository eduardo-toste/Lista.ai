package com.listaai.recipe.adapter.inbound.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.listaai.recipe.adapter.inbound.web.mapper.RecipeWebMapper;
import com.listaai.recipe.adapter.inbound.web.request.RecipeRequest;
import com.listaai.recipe.application.dto.output.RecipeItemsOutput;
import com.listaai.recipe.application.port.inbound.ExtractRecipeItemsUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RecipeController.class)
@Import(RecipeWebMapper.class)
class RecipeControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ExtractRecipeItemsUseCase extractRecipeItemsUseCase;

    @Test
    void shouldExtractRecipeItems() throws Exception {
        RecipeItemsOutput output = new RecipeItemsOutput(List.of(
                new RecipeItemsOutput.ShoppingListRecipeItem(null, "Frango", 1, "KG", false)
        ));
        when(extractRecipeItemsUseCase.extractItems(any())).thenReturn(output);

        RecipeRequest request = new RecipeRequest("strogonoff de frango");

        mockMvc.perform(post("/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].name").value("Frango"))
                .andExpect(jsonPath("$.items[0].quantity").value(1))
                .andExpect(jsonPath("$.items[0].unit").value("KG"))
                .andExpect(jsonPath("$.items[0].purchased").value(false));

        verify(extractRecipeItemsUseCase).extractItems(any());
    }

    @Test
    void shouldReturnBadRequestWhenRecipeMessageIsBlank() throws Exception {
        RecipeRequest request = new RecipeRequest("");

        mockMvc.perform(post("/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(extractRecipeItemsUseCase, never()).extractItems(any());
    }
}
