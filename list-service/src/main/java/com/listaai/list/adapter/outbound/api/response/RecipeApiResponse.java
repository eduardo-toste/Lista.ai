package com.listaai.list.adapter.outbound.api.response;

import java.util.List;

public record RecipeApiResponse(

        List<RecipeItemResponse> items

) {

    public record RecipeItemResponse(
            String name,
            int quantity,
            String unit,
            boolean purchased
    ) {
    }

}
