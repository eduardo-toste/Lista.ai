package com.listaai.recipe.adapter.inbound.web.mapper;

import com.listaai.recipe.adapter.inbound.web.response.AiRecipeItemsResponse;
import com.listaai.recipe.application.dto.output.RecipeItemsOutput;
import org.springframework.stereotype.Component;

@Component
public class RecipeWebMapper {

    public AiRecipeItemsResponse toResponse(RecipeItemsOutput output) {
        return new AiRecipeItemsResponse(
                output.items().stream()
                        .map(item -> new AiRecipeItemsResponse.AiRecipeItem(
                                item.name(),
                                item.quantity(),
                                item.unit(),
                                item.purchased()))
                        .toList());
    }

}
