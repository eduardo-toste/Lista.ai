package com.listaai.list.adapter.outbound.api.adapter;

import com.listaai.list.adapter.outbound.api.client.RecipeApiClient;
import com.listaai.list.adapter.outbound.api.mapper.ApiWebMapper;
import com.listaai.list.adapter.outbound.api.request.RecipeApiRequest;
import com.listaai.list.adapter.outbound.api.response.RecipeApiResponse;
import com.listaai.list.application.port.outbound.RecipeExtractionPort;
import com.listaai.list.domain.model.ShoppingListItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecipeExtractionAdapter implements RecipeExtractionPort {

    private final RecipeApiClient client;
    private final ApiWebMapper apiWebMapper;

    @Override
    public List<ShoppingListItem> extractItemsFromRecipe(String recipeMessage) {
        RecipeApiResponse response = client.extractItems(new RecipeApiRequest(recipeMessage));
        return apiWebMapper.toDomain(response);
    }

}
