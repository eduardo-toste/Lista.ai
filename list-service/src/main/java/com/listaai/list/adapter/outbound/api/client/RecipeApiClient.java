package com.listaai.list.adapter.outbound.api.client;

import com.listaai.list.adapter.outbound.api.request.RecipeApiRequest;
import com.listaai.list.adapter.outbound.api.response.RecipeApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "recipeService", url = "${clients.recipe-service.url}")
public interface RecipeApiClient {

    @PostMapping(path = "/recipe")
    RecipeApiResponse extractItems(@RequestBody RecipeApiRequest request);

}
