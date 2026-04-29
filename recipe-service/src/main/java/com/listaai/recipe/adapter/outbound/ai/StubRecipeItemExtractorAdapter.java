package com.listaai.recipe.adapter.outbound.ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.listaai.recipe.adapter.outbound.ai.mapper.RecipeAiMapper;
import com.listaai.recipe.application.dto.input.RecipeMessageCommand;
import com.listaai.recipe.application.dto.output.RecipeItemsOutput;
import com.listaai.recipe.application.port.outbound.RecipeItemExtractorPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StubRecipeItemExtractorAdapter implements RecipeItemExtractorPort {

    private final Client client;
    private final RecipePromptBuilder recipePromptBuilder;
    private final RecipeAiMapper recipeAiMapper;

    @Value("${spring.ai.google.genai.chat.options.model}")
    private String model;

    @Override
    public RecipeItemsOutput extractFrom(RecipeMessageCommand command) {
        String prompt = recipePromptBuilder.build(command.message());

        GenerateContentResponse response = client.models.generateContent(
                model,
                prompt,
                null
        );

        return recipeAiMapper.toOutput(response.text());
    }
}
