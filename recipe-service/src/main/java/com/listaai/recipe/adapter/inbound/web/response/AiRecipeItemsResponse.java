package com.listaai.recipe.adapter.inbound.web.response;

import java.util.List;

public record AiRecipeItemsResponse(

        List<AiRecipeItem> items

) {

    public record AiRecipeItem(
            String name,
            int quantity,
            String unit,
            boolean purchased
    ) {
    }

}
