package com.listaai.recipe.adapter.outbound.ai.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecipeAiMapperTest {

    private final RecipeAiMapper mapper = new RecipeAiMapper(new ObjectMapper());

    @Test
    void shouldParseAiJsonIntoOutput() {
        String json = """
                {
                  "items": [
                    {
                      "name": "Frango",
                      "quantity": 1,
                      "unit": "KG",
                      "purchased": false
                    }
                  ]
                }
                """;

        var result = mapper.toOutput(json);

        assertThat(result.items()).singleElement().satisfies(item -> {
            assertThat(item.name()).isEqualTo("Frango");
            assertThat(item.quantity()).isEqualTo(1);
            assertThat(item.unit()).isEqualTo("KG");
            assertThat(item.purchased()).isFalse();
            assertThat(item.id()).isNull();
        });
    }

    @Test
    void shouldThrowWhenAiJsonIsInvalid() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> mapper.toOutput("not-json")
        );

        assertThat(exception.getMessage()).isEqualTo("Failed to parse AI response: not-json");
        assertInstanceOf(Exception.class, exception.getCause());
    }
}
