package com.listaai.list.adapter.inbound.web.controller;

import com.listaai.list.adapter.inbound.web.mapper.ShoppingListItemWebMapper;
import com.listaai.list.adapter.inbound.web.mapper.ShoppingListWebMapper;
import com.listaai.list.adapter.inbound.web.request.CreateShoppingListItemRequest;
import com.listaai.list.adapter.inbound.web.response.ShoppingListResponse;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.port.inbound.items.AddItemToListUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lists/{listId}/items")
@RequiredArgsConstructor
public class ShoppingListItemController {

    private final AddItemToListUseCase addItemToListUseCase;
    private final ShoppingListItemWebMapper shoppingListItemWebMapper;
    private final ShoppingListWebMapper shoppingListWebMapper;

    @PostMapping
    public ResponseEntity<ShoppingListResponse> addItemToList(@PathVariable Long listId, @RequestBody CreateShoppingListItemRequest itemRequest) {
        ShoppingListOutput output = addItemToListUseCase.addItemToShoppingList(listId, shoppingListItemWebMapper.toCommand(itemRequest));
        ShoppingListResponse response = shoppingListWebMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

}
