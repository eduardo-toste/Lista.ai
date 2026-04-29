package com.listaai.list.application.port.outbound;

import com.listaai.list.domain.model.ShoppingListItem;

import java.util.List;

public interface RecipeExtractionPort {

    List<ShoppingListItem> extractItemsFromRecipe(String recipeMessage);

}
