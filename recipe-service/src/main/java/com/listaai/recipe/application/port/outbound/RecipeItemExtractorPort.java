package com.listaai.recipe.application.port.outbound;

import com.listaai.recipe.application.dto.input.RecipeMessageCommand;
import com.listaai.recipe.application.dto.output.RecipeItemsOutput;

public interface RecipeItemExtractorPort {

    RecipeItemsOutput extractFrom(RecipeMessageCommand command);

}
