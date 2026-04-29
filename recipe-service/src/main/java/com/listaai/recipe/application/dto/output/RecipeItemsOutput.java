package com.listaai.recipe.application.dto.output;

import java.util.List;

public record RecipeItemsOutput(

        List<ShoppingListRecipeItem> items

) {

    public record ShoppingListRecipeItem(
            Long id,
            String name,
            int quantity,
            String unit,
            boolean purchased
    ) {
    }

}
