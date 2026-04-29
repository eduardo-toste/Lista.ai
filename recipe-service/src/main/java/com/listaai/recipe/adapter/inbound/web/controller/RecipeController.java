package com.listaai.recipe.adapter.inbound.web.controller;

import com.listaai.recipe.adapter.inbound.web.mapper.RecipeWebMapper;
import com.listaai.recipe.adapter.inbound.web.request.RecipeRequest;
import com.listaai.recipe.adapter.inbound.web.response.AiRecipeItemsResponse;
import com.listaai.recipe.application.dto.input.RecipeMessageCommand;
import com.listaai.recipe.application.dto.output.RecipeItemsOutput;
import com.listaai.recipe.application.port.inbound.ExtractRecipeItemsUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final ExtractRecipeItemsUseCase extractRecipeItemsUseCase;
    private final RecipeWebMapper recipeWebMapper;

    @PostMapping
    public ResponseEntity<AiRecipeItemsResponse> getItems(@RequestBody @Valid RecipeRequest request) {
        RecipeItemsOutput output = extractRecipeItemsUseCase.extractItems(new RecipeMessageCommand(request.recipeMessage()));
        return ResponseEntity.ok(recipeWebMapper.toResponse(output));
    }

}
