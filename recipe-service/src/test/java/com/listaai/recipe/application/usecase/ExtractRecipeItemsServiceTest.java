package com.listaai.recipe.application.usecase;

import com.listaai.recipe.application.dto.input.RecipeMessageCommand;
import com.listaai.recipe.application.dto.output.RecipeItemsOutput;
import com.listaai.recipe.application.port.outbound.RecipeItemExtractorPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExtractRecipeItemsServiceTest {

    @Mock
    private RecipeItemExtractorPort recipeItemExtractorPort;

    @InjectMocks
    private ExtractRecipeItemsService service;

    @Test
    void shouldDelegateExtractionToPort() {
        RecipeMessageCommand command = new RecipeMessageCommand("strogonoff de frango");
        RecipeItemsOutput output = new RecipeItemsOutput(List.of());
        when(recipeItemExtractorPort.extractFrom(command)).thenReturn(output);

        RecipeItemsOutput result = service.extractItems(command);

        assertSame(output, result);
        verify(recipeItemExtractorPort).extractFrom(command);
    }
}
