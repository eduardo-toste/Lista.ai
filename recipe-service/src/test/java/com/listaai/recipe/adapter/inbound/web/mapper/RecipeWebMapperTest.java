package com.listaai.recipe.adapter.inbound.web.mapper;

import com.listaai.recipe.application.dto.output.RecipeItemsOutput;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeWebMapperTest {

    private final RecipeWebMapper mapper = new RecipeWebMapper();

    @Test
    void shouldMapOutputToResponse() {
        RecipeItemsOutput output = new RecipeItemsOutput(List.of(
                new RecipeItemsOutput.ShoppingListRecipeItem(null, "Frango", 1, "KG", false)
        ));

        var response = mapper.toResponse(output);

        assertThat(response.items()).singleElement().satisfies(item -> {
            assertThat(item.name()).isEqualTo("Frango");
            assertThat(item.quantity()).isEqualTo(1);
            assertThat(item.unit()).isEqualTo("KG");
            assertThat(item.purchased()).isFalse();
        });
    }
}
