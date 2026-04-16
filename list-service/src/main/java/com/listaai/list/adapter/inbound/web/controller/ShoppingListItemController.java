package com.listaai.list.adapter.inbound.web.controller;

import com.listaai.list.adapter.inbound.web.mapper.ShoppingListItemWebMapper;
import com.listaai.list.adapter.inbound.web.mapper.ShoppingListWebMapper;
import com.listaai.list.adapter.inbound.web.request.CreateShoppingListItemRequest;
import com.listaai.list.adapter.inbound.web.request.PurchaseItemRequest;
import com.listaai.list.adapter.inbound.web.request.UpdateShoppingListItemRequest;
import com.listaai.list.adapter.inbound.web.response.ShoppingListResponse;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.port.inbound.items.AddItemToListUseCase;
import com.listaai.list.application.port.inbound.items.PurchaseItemFromListUseCase;
import com.listaai.list.application.port.inbound.items.RemoveItemFromListUseCase;
import com.listaai.list.application.port.inbound.items.UpdateItemFromListUseCase;
import com.listaai.list.domain.model.ShoppingList;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lists/{listId}/items")
@RequiredArgsConstructor
public class ShoppingListItemController {

    private final AddItemToListUseCase addItemToListUseCase;
    private final RemoveItemFromListUseCase removeItemFromListUseCase;
    private final UpdateItemFromListUseCase updateItemFromListUseCase;
    private final PurchaseItemFromListUseCase purchaseItemFromListUseCase;
    private final ShoppingListItemWebMapper shoppingListItemWebMapper;
    private final ShoppingListWebMapper shoppingListWebMapper;

    @PostMapping
    public ResponseEntity<ShoppingListResponse> addItemToList(@PathVariable Long listId, @RequestBody CreateShoppingListItemRequest itemRequest) {
        ShoppingListOutput output = addItemToListUseCase.addItemToShoppingList(listId, shoppingListItemWebMapper.toCommand(itemRequest));
        ShoppingListResponse response = shoppingListWebMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{itemId}")
    public ResponseEntity<ShoppingListResponse> removeItemFromList(@PathVariable Long listId, @PathVariable Long itemId) {
        ShoppingListOutput output = removeItemFromListUseCase.removeItemFromShoppingList(listId, itemId);
        ShoppingListResponse response = shoppingListWebMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<ShoppingListResponse> updateItemFromList(@PathVariable Long listId, @PathVariable Long itemId, @RequestBody UpdateShoppingListItemRequest request) {
        ShoppingListOutput output = updateItemFromListUseCase.updateItem(listId, itemId, shoppingListItemWebMapper.toCommand(request));
        ShoppingListResponse response = shoppingListWebMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{itemId}/purchase")
    public ResponseEntity<ShoppingListResponse> purchaseItem(@PathVariable Long listId, @PathVariable Long itemId, @RequestBody PurchaseItemRequest request) {
        ShoppingListOutput output = purchaseItemFromListUseCase.purchaseItemFromList(listId, itemId, request.purchased());
        ShoppingListResponse response = shoppingListWebMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

}
