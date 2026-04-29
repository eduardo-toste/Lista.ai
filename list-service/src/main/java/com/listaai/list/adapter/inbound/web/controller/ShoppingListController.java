package com.listaai.list.adapter.inbound.web.controller;

import com.listaai.list.adapter.inbound.web.mapper.ShoppingListWebMapper;
import com.listaai.list.adapter.inbound.web.request.CreateShoppingListRequest;
import com.listaai.list.adapter.inbound.web.request.CreateSmartShoppingListRequest;
import com.listaai.list.adapter.inbound.web.request.UpdateShoppingListNameRequest;
import com.listaai.list.adapter.inbound.web.response.ShoppingListResponse;
import com.listaai.list.application.dto.input.ShoppingListCommand;
import com.listaai.list.application.dto.input.SmartShoppingListCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.port.inbound.lists.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Shopping Lists", description = "Operations for managing shopping lists")
@RestController
@RequestMapping("/lists")
@RequiredArgsConstructor
public class ShoppingListController {

    private final CreateShoppingListUseCase createShoppingListUseCase;
    private final CreateSmartShoppingListUseCase createSmartShoppingListUseCase;
    private final GetShoppingListUseCase getShoppingListUseCase;
    private final UpdateListNameUseCase updateListNameUseCase;
    private final DeleteShoppingListUseCase deleteShoppingListUseCase;
    private final ShareShoppingListUseCase shareShoppingListUseCase;
    private final ShoppingListWebMapper shoppingListMapper;

    @Operation(
            summary = "Create a shopping list",
            description = "Creates a new shopping list. Items and participants can optionally be included in the request body."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shopping list created successfully",
                    content = @Content(schema = @Schema(implementation = ShoppingListResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ShoppingListResponse> createShoppingList(@RequestBody @Valid CreateShoppingListRequest request) {
        ShoppingListCommand command = shoppingListMapper.toCommand(request);
        ShoppingListOutput output = createShoppingListUseCase.createShoppingList(command);
        ShoppingListResponse response = shoppingListMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/smart")
    public ResponseEntity<ShoppingListResponse> createSmartShoppingList(@RequestBody @Valid CreateSmartShoppingListRequest request) {
        SmartShoppingListCommand command = shoppingListMapper.toCommand(request);
        ShoppingListOutput output = createSmartShoppingListUseCase.createSmartShoppingList(command);
        ShoppingListResponse response = shoppingListMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get a shopping list by ID",
            description = "Returns a single shopping list with its items and participants."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shopping list found",
                    content = @Content(schema = @Schema(implementation = ShoppingListResponse.class))),
            @ApiResponse(responseCode = "404", description = "Shopping list not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("{id}")
    public ResponseEntity<ShoppingListResponse> getShoppingListById(
            @Parameter(description = "ID of the shopping list", required = true) @PathVariable Long id) {
        ShoppingListOutput output = getShoppingListUseCase.getShoppingListById(id);
        ShoppingListResponse response = shoppingListMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "List all shopping lists",
            description = "Returns a paginated list of all shopping lists with their items and participants."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of shopping lists returned successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<ShoppingListResponse>> getShoppingLists(Pageable pageable) {
        Page<ShoppingListOutput> pageOutput = getShoppingListUseCase.getShoppingLists(pageable);
        Page<ShoppingListResponse> pageResponse = shoppingListMapper.toPageResponse(pageOutput);
        return ResponseEntity.ok(pageResponse);
    }

    @Operation(
            summary = "Update a shopping list name",
            description = "Updates the name of an existing shopping list."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shopping list name updated successfully",
                    content = @Content(schema = @Schema(implementation = ShoppingListResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "404", description = "Shopping list not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PatchMapping("{id}")
    public ResponseEntity<ShoppingListResponse> updateShoppingListName(
            @Parameter(description = "ID of the shopping list", required = true) @PathVariable Long id,
            @RequestBody @Valid UpdateShoppingListNameRequest request) {
        ShoppingListOutput output = updateListNameUseCase.updateName(id, request.name());
        ShoppingListResponse response = shoppingListMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete a shopping list",
            description = "Permanently deletes a shopping list along with all its items and participants."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shopping list deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Shopping list not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteShoppingList(
            @Parameter(description = "ID of the shopping list", required = true) @PathVariable Long id) {
        deleteShoppingListUseCase.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{id}/share")
    public ResponseEntity<Void> shareShoppingList(@PathVariable Long id) {
        shareShoppingListUseCase.shareShoppingList(id);
        return ResponseEntity.ok().build();
    }

}
