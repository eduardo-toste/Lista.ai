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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lists/{listId}/participants")
@RequiredArgsConstructor
public class ShoppingListParticipantController {

    private final AddParticipantToListUseCase addParticipantToListUseCase;
    private final UpdateParticipantFromListUseCase updateParticipantFromListUseCase;
    private final RemoveParticipantFromListUseCase removeParticipantFromListUseCase;
    private final ShoppingListParticipantWebMapper shoppingListParticipantWebMapper;
    private final ShoppingListWebMapper shoppingListWebMapper;

    @PostMapping
    public ResponseEntity<ShoppingListResponse> addParticipantToList(@PathVariable Long listId, @RequestBody CreateShoppingListParticipantRequest request) {
        ShoppingListOutput output = addParticipantToListUseCase.addParticipantToShoppingList(listId, shoppingListParticipantWebMapper.toCommand(request));
        ShoppingListResponse response = shoppingListWebMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{participantId}")
    public ResponseEntity<ShoppingListResponse> updateParticipantFromList(@PathVariable Long listId, @PathVariable Long participantId, @RequestBody UpdateShoppingListParticipantRequest request) {
        ShoppingListOutput output = updateParticipantFromListUseCase.updateParticipantFromShoppingListUseCase(listId, participantId, shoppingListParticipantWebMapper.toCommand(request));
        ShoppingListResponse response = shoppingListWebMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{participantId}")
    public ResponseEntity<ShoppingListResponse> removeParticipantFromList(@PathVariable Long listId, @PathVariable Long participantId) {
        ShoppingListOutput output = removeParticipantFromListUseCase.removeParticipantFromShoppingList(listId, participantId);
        ShoppingListResponse response = shoppingListWebMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

}
