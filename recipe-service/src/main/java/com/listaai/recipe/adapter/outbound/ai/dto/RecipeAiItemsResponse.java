package com.listaai.recipe.adapter.outbound.ai.dto;

import java.util.List;

public record RecipeAiItemsResponse(

        List<RecipeAiItem> items

) {

    public record RecipeAiItem(
            String name,
            int quantity,
            String unit,
            boolean purchased
    ) {
    }

}
