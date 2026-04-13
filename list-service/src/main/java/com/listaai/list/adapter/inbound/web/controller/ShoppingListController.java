package com.listaai.list.adapter.inbound.web.controller;

import com.listaai.list.adapter.inbound.web.mapper.ShoppingListMapper;
import com.listaai.list.adapter.inbound.web.request.CreateShoppingListRequest;
import com.listaai.list.adapter.inbound.web.response.ShoppingListResponse;
import com.listaai.list.application.dto.input.CreateShoppingListCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.port.inbound.CreateShoppingListUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/list")
@RequiredArgsConstructor
public class ShoppingListController {

    private final CreateShoppingListUseCase createShoppingListUseCase;
    private final ShoppingListMapper shoppingListMapper;

    @PostMapping
    public ResponseEntity<ShoppingListResponse> createShoppingList(@RequestBody @Valid CreateShoppingListRequest request) {
        CreateShoppingListCommand command = shoppingListMapper.toCommand(request);
        ShoppingListOutput output = createShoppingListUseCase.createShoppingList(command);
        ShoppingListResponse response = shoppingListMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

}
