package com.listaai.recipe.adapter.inbound.web.request;

import jakarta.validation.constraints.NotBlank;

public record RecipeRequest(

        @NotBlank(message = "Recipe message must not be blank")
        String recipeMessage

) {
}
