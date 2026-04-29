package com.listaai.recipe.adapter.outbound.ai.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.listaai.recipe.adapter.outbound.ai.dto.RecipeAiItemsResponse;
import com.listaai.recipe.application.dto.output.RecipeItemsOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecipeAiMapper {

    private final ObjectMapper objectMapper;

    public RecipeItemsOutput toOutput(String json) {
        try {
            RecipeAiItemsResponse response = objectMapper.readValue(json, RecipeAiItemsResponse.class);

            return new RecipeItemsOutput(
                    response.items().stream()
                            .map(item -> new RecipeItemsOutput.ShoppingListRecipeItem(
                                    null,
                                    item.name(),
                                    item.quantity(),
                                    item.unit(),
                                    item.purchased()))
                            .toList()
            );
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse AI response: " + json, e);
        }
    }
}
