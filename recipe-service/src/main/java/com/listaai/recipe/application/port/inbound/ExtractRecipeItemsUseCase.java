package com.listaai.recipe.application.port.inbound;

import com.listaai.recipe.application.dto.input.RecipeMessageCommand;
import com.listaai.recipe.application.dto.output.RecipeItemsOutput;

public interface ExtractRecipeItemsUseCase {

    RecipeItemsOutput extractItems(RecipeMessageCommand command);

}
