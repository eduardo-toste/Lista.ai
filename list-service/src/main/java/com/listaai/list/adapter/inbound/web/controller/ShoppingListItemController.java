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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Shopping List Items", description = "Operations for managing items within a shopping list")
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

    @Operation(
            summary = "Add an item to a shopping list",
            description = "Adds a new item to the specified shopping list."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item added successfully",
                    content = @Content(schema = @Schema(implementation = ShoppingListResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "404", description = "Shopping list not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ShoppingListResponse> addItemToList(
            @Parameter(description = "ID of the shopping list", required = true) @PathVariable Long listId,
            @RequestBody @Valid CreateShoppingListItemRequest itemRequest) {
        ShoppingListOutput output = addItemToListUseCase.addItemToShoppingList(listId, shoppingListItemWebMapper.toCommand(itemRequest));
        ShoppingListResponse response = shoppingListWebMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Remove an item from a shopping list",
            description = "Removes an existing item from the specified shopping list."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item removed successfully",
                    content = @Content(schema = @Schema(implementation = ShoppingListResponse.class))),
            @ApiResponse(responseCode = "404", description = "Shopping list or item not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("{itemId}")
    public ResponseEntity<ShoppingListResponse> removeItemFromList(
            @Parameter(description = "ID of the shopping list", required = true) @PathVariable Long listId,
            @Parameter(description = "ID of the item to remove", required = true) @PathVariable Long itemId) {
        ShoppingListOutput output = removeItemFromListUseCase.removeItemFromShoppingList(listId, itemId);
        ShoppingListResponse response = shoppingListWebMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update an item in a shopping list",
            description = "Updates the name, quantity or unit of an existing item. Only provided fields will be updated."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item updated successfully",
                    content = @Content(schema = @Schema(implementation = ShoppingListResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "404", description = "Shopping list or item not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PatchMapping("{itemId}")
    public ResponseEntity<ShoppingListResponse> updateItemFromList(
            @Parameter(description = "ID of the shopping list", required = true) @PathVariable Long listId,
            @Parameter(description = "ID of the item to update", required = true) @PathVariable Long itemId,
            @RequestBody @Valid UpdateShoppingListItemRequest request) {
        ShoppingListOutput output = updateItemFromListUseCase.updateItem(listId, itemId, shoppingListItemWebMapper.toCommand(request));
        ShoppingListResponse response = shoppingListWebMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Mark an item as purchased or not purchased",
            description = "Updates the purchased status of an item in the shopping list."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item purchase status updated successfully",
                    content = @Content(schema = @Schema(implementation = ShoppingListResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "404", description = "Shopping list or item not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PatchMapping("{itemId}/purchase")
    public ResponseEntity<ShoppingListResponse> purchaseItem(
            @Parameter(description = "ID of the shopping list", required = true) @PathVariable Long listId,
            @Parameter(description = "ID of the item to update", required = true) @PathVariable Long itemId,
            @RequestBody @Valid PurchaseItemRequest request) {
        ShoppingListOutput output = purchaseItemFromListUseCase.purchaseItemFromList(listId, itemId, request.purchased());
        ShoppingListResponse response = shoppingListWebMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

}
