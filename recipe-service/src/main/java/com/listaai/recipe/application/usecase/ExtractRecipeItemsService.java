package com.listaai.recipe.application.usecase;

import com.listaai.recipe.application.dto.input.RecipeMessageCommand;
import com.listaai.recipe.application.dto.output.RecipeItemsOutput;
import com.listaai.recipe.application.port.inbound.ExtractRecipeItemsUseCase;
import com.listaai.recipe.application.port.outbound.RecipeItemExtractorPort;

public class ExtractRecipeItemsService implements ExtractRecipeItemsUseCase {

    private final RecipeItemExtractorPort recipeItemExtractorPort;

    public ExtractRecipeItemsService(RecipeItemExtractorPort recipeItemExtractorPort) {
        this.recipeItemExtractorPort = recipeItemExtractorPort;
    }

    @Override
    public RecipeItemsOutput extractItems(RecipeMessageCommand command) {
        return recipeItemExtractorPort.extractFrom(command);
    }

}
