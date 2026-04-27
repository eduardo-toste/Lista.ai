package com.listaai.list.adapter.inbound.web.controller;

import com.listaai.list.adapter.inbound.web.mapper.ShoppingListParticipantWebMapper;
import com.listaai.list.adapter.inbound.web.mapper.ShoppingListWebMapper;
import com.listaai.list.adapter.inbound.web.request.CreateShoppingListParticipantRequest;
import com.listaai.list.adapter.inbound.web.request.UpdateShoppingListParticipantRequest;
import com.listaai.list.adapter.inbound.web.response.ShoppingListResponse;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.port.inbound.participants.AddParticipantToListUseCase;
import com.listaai.list.application.port.inbound.participants.RemoveParticipantFromListUseCase;
import com.listaai.list.application.port.inbound.participants.UpdateParticipantFromListUseCase;
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

@Tag(name = "Shopping List Participants", description = "Operations for managing participants within a shopping list")
@RestController
@RequestMapping("/lists/{listId}/participants")
@RequiredArgsConstructor
public class ShoppingListParticipantController {

    private final AddParticipantToListUseCase addParticipantToListUseCase;
    private final UpdateParticipantFromListUseCase updateParticipantFromListUseCase;
    private final RemoveParticipantFromListUseCase removeParticipantFromListUseCase;
    private final ShoppingListParticipantWebMapper shoppingListParticipantWebMapper;
    private final ShoppingListWebMapper shoppingListWebMapper;

    @Operation(
            summary = "Add a participant to a shopping list",
            description = "Adds a new participant to the specified shopping list. Phone number must contain only digits and have 10 or 11 characters."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participant added successfully",
                    content = @Content(schema = @Schema(implementation = ShoppingListResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "404", description = "Shopping list not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ShoppingListResponse> addParticipantToList(
            @Parameter(description = "ID of the shopping list", required = true) @PathVariable Long listId,
            @RequestBody @Valid CreateShoppingListParticipantRequest request) {
        ShoppingListOutput output = addParticipantToListUseCase.addParticipantToShoppingList(listId, shoppingListParticipantWebMapper.toCommand(request));
        ShoppingListResponse response = shoppingListWebMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update a participant in a shopping list",
            description = "Updates the name or phone number of an existing participant. Only provided fields will be updated."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participant updated successfully",
                    content = @Content(schema = @Schema(implementation = ShoppingListResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "404", description = "Shopping list or participant not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PatchMapping("{participantId}")
    public ResponseEntity<ShoppingListResponse> updateParticipantFromList(
            @Parameter(description = "ID of the shopping list", required = true) @PathVariable Long listId,
            @Parameter(description = "ID of the participant to update", required = true) @PathVariable Long participantId,
            @RequestBody @Valid UpdateShoppingListParticipantRequest request) {
        ShoppingListOutput output = updateParticipantFromListUseCase.updateParticipantFromShoppingListUseCase(listId, participantId, shoppingListParticipantWebMapper.toCommand(request));
        ShoppingListResponse response = shoppingListWebMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Remove a participant from a shopping list",
            description = "Removes an existing participant from the specified shopping list."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participant removed successfully",
                    content = @Content(schema = @Schema(implementation = ShoppingListResponse.class))),
            @ApiResponse(responseCode = "404", description = "Shopping list or participant not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("{participantId}")
    public ResponseEntity<ShoppingListResponse> removeParticipantFromList(
            @Parameter(description = "ID of the shopping list", required = true) @PathVariable Long listId,
            @Parameter(description = "ID of the participant to remove", required = true) @PathVariable Long participantId) {
        ShoppingListOutput output = removeParticipantFromListUseCase.removeParticipantFromShoppingList(listId, participantId);
        ShoppingListResponse response = shoppingListWebMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

}
