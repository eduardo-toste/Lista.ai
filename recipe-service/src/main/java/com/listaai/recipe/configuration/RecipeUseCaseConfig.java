package com.listaai.recipe.configuration;

import com.listaai.recipe.application.port.inbound.ExtractRecipeItemsUseCase;
import com.listaai.recipe.application.port.outbound.RecipeItemExtractorPort;
import com.listaai.recipe.application.usecase.ExtractRecipeItemsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecipeUseCaseConfig {

    @Bean
    public ExtractRecipeItemsUseCase extractRecipeItemsUseCase(RecipeItemExtractorPort recipeItemExtractorPort) {
        return new ExtractRecipeItemsService(recipeItemExtractorPort);
    }

}
