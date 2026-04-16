package com.listaai.list.adapter.inbound.web.controller;

import com.listaai.list.adapter.inbound.web.mapper.ShoppingListWebMapper;
import com.listaai.list.adapter.inbound.web.request.CreateShoppingListRequest;
import com.listaai.list.adapter.inbound.web.request.UpdateShoppingListNameRequest;
import com.listaai.list.adapter.inbound.web.response.ShoppingListResponse;
import com.listaai.list.application.dto.input.CreateShoppingListCommand;
import com.listaai.list.application.dto.output.ShoppingListOutput;
import com.listaai.list.application.port.inbound.lists.CreateShoppingListUseCase;
import com.listaai.list.application.port.inbound.lists.GetShoppingListUseCase;
import com.listaai.list.application.port.inbound.lists.UpdateListNameUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lists")
@RequiredArgsConstructor
public class ShoppingListController {

    private final CreateShoppingListUseCase createShoppingListUseCase;
    private final GetShoppingListUseCase getShoppingListUseCase;
    private final UpdateListNameUseCase updateListNameUseCase;
    private final ShoppingListWebMapper shoppingListMapper;

    @PostMapping
    public ResponseEntity<ShoppingListResponse> createShoppingList(@RequestBody @Valid CreateShoppingListRequest request) {
        CreateShoppingListCommand command = shoppingListMapper.toCommand(request);
        ShoppingListOutput output = createShoppingListUseCase.createShoppingList(command);
        ShoppingListResponse response = shoppingListMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<ShoppingListResponse> getShoppingListById(@PathVariable Long id) {
        ShoppingListOutput output = getShoppingListUseCase.getShoppingListById(id);
        ShoppingListResponse response = shoppingListMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ShoppingListResponse>> getShoppingLists(Pageable pageable) {
        Page<ShoppingListOutput> pageOutput = getShoppingListUseCase.getShoppingLists(pageable);
        Page<ShoppingListResponse> pageResponse = shoppingListMapper.toPageResponse(pageOutput);
        return ResponseEntity.ok(pageResponse);
    }

    @PatchMapping("{id}")
    public ResponseEntity<ShoppingListResponse> updateShoppingListName(@PathVariable Long id, @RequestBody UpdateShoppingListNameRequest request) {
        ShoppingListOutput output = updateListNameUseCase.updateName(id, request.name());
        ShoppingListResponse response = shoppingListMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }
}
