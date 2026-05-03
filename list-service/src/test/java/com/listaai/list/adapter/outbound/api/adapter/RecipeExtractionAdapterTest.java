package com.listaai.list.adapter.outbound.api.adapter;

import com.listaai.list.adapter.outbound.api.client.RecipeApiClient;
import com.listaai.list.adapter.outbound.api.mapper.ApiWebMapper;
import com.listaai.list.adapter.outbound.api.request.RecipeApiRequest;
import com.listaai.list.adapter.outbound.api.response.RecipeApiResponse;
import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.model.ShoppingListItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeExtractionAdapterTest {

    @Mock
    private RecipeApiClient client;

    @Mock
    private ApiWebMapper apiWebMapper;

    @InjectMocks
    private RecipeExtractionAdapter recipeExtractionAdapter;

    @Test
    void shouldCallRecipeClientAndMapResponseToDomain() {
        RecipeApiResponse response = new RecipeApiResponse(List.of(
                new RecipeApiResponse.RecipeItemResponse("Tomate", 2, "UN", false)
        ));
        List<ShoppingListItem> mappedItems = List.of(
                new ShoppingListItem(null, "Tomate", 2, ItemUnit.UN, false)
        );
        when(client.extractItems(org.mockito.ArgumentMatchers.any(RecipeApiRequest.class))).thenReturn(response);
        when(apiWebMapper.toDomain(response)).thenReturn(mappedItems);

        List<ShoppingListItem> result = recipeExtractionAdapter.extractItemsFromRecipe("ingredientes para salada");

        assertSame(mappedItems, result);

        ArgumentCaptor<RecipeApiRequest> requestCaptor = ArgumentCaptor.forClass(RecipeApiRequest.class);
        verify(client).extractItems(requestCaptor.capture());
        assertThat(requestCaptor.getValue().recipeMessage()).isEqualTo("ingredientes para salada");
        verify(apiWebMapper).toDomain(response);
    }
}
