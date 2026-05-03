package com.listaai.recipe.adapter.outbound.ai;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecipePromptBuilderTest {

    private final RecipePromptBuilder recipePromptBuilder = new RecipePromptBuilder();

    @Test
    void shouldBuildPromptEmbeddingTrimmedRecipe() {
        String prompt = recipePromptBuilder.build("  strogonoff de frango  ");

        assertThat(prompt).contains("Extraia todos os ingredientes necessarios");
        assertThat(prompt).contains("<<<RECIPE>>>\nstrogonoff de frango\n<<<END_RECIPE>>>");
        assertThat(prompt).doesNotContain("  strogonoff de frango  ");
    }

    @Test
    void shouldBuildPromptWithEmptyRecipeWhenInputIsNull() {
        String prompt = recipePromptBuilder.build(null);

        assertThat(prompt).contains("<<<RECIPE>>>\n\n<<<END_RECIPE>>>");
    }
}
